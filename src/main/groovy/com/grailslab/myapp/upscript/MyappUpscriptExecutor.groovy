package com.grailslab.myapp.upscript

import com.grailslab.myapp.config.ConfigKey
import groovy.sql.Sql
import org.springframework.core.env.Environment
import org.springframework.core.io.DefaultResourceLoader
import org.springframework.core.io.ResourceLoader

/**
 * Created by Aminul on 4/1/2017.
 */
trait MyappUpscriptExecutor {
    static ResourceLoader defaultResourceLoader = new DefaultResourceLoader()

    void executeUpscript(Environment environment) {
        Sql sql = getSqlInstance(environment)
        execute(environment, sql)
        sql.close()
    }

    private Sql getSqlInstance(Environment environment) {
        String url = environment.getProperty(ConfigKey.DATASOURCE_URL)
        String username = environment.getProperty(ConfigKey.DATASOURCE_USERNAME)
        String password = environment.getProperty(ConfigKey.DATASOURCE_PASSWORD)
        String driver = environment.getProperty(ConfigKey.GLOBAL_DATABASE_DRIVER)
        def props = [user: username, password: password, allowMultiQueries: 'true'] as Properties
        Sql sql = Sql.newInstance(url, props, driver)
        return sql
    }

    private void execute(Environment environment, Sql sql) {
        String scriptResource = "classpath:upscript"
        File scriptRootDir = defaultResourceLoader.getResource(scriptResource).file
        List<File> scriptDirList = scriptRootDir.listFiles()?.toList()
        if (!scriptDirList) return
        // find plugin directory with valid name
        scriptDirList = scriptDirList.findAll {
            it.directory && it.name.matches("^[\\d].*_.*") // starts with {digit}_
        }

        //sort by plugin id
        scriptDirList = scriptDirList.sort { a, b ->
            long n1 = Long.parseLong(a.name.substring(0, a.name.indexOf("_")))
            long n2 = Long.parseLong(b.name.substring(0, b.name.indexOf("_")))
            return n1 <=> n2
        }

        for (File scriptDir : scriptDirList) {
            List<File> scriptFiles = scriptDir.listFiles()?.toList()
            if (!scriptFiles) continue

            Long pluginId = Long.parseLong(scriptDir.name.substring(0, scriptDir.name.indexOf("_")))
            String pluginName = scriptDir.name.substring(scriptDir.name.indexOf("_") + 1)

            // find sql files with valid name
            List<File> sqlFiles = scriptFiles.findAll {
                it.name.matches("^[\\d].*\\..*") && it.name.endsWith(".sql")
            }
            if (sqlFiles) {
                sqlFiles = sqlFiles.sort { a, b ->
                    long n1 = Long.parseLong(a.name.substring(0, a.name.indexOf(".sql")))
                    long n2 = Long.parseLong(b.name.substring(0, b.name.indexOf(".sql")))
                    return n1 <=> n2
                }

                Long version = getLastVersion(sql, pluginId)
                List<File> sqlNewFiles = sqlFiles.findAll {
                    Long.parseLong(it.name.substring(0, it.name.indexOf(".sql"))) > version
                }
                for (File sqlFile : sqlNewFiles) {
                    println("applying script: " + sqlFile.name + "\t\t>\tplugin - " + pluginName)
                    try {
                        Long versionNumber = Long.parseLong(sqlFile.name.substring(0, sqlFile.name.indexOf(".sql")))
                        sql.withTransaction {
                            sql.execute(sqlFile.getText("UTF-8"))
                            sql.executeUpdate("INSERT INTO core_version(plugin_id, version_number) VALUES (${pluginId}, ${versionNumber})")
                        }
                    } catch (Exception ex) {
                        println("failed to apply script: " + sqlFile.name + "\t\t>\tplugin - " + pluginName)
                        println("rolling back script: " + sqlFile.name + "\t\t>\tplugin - " + pluginName)
                        throw ex
                    }
                }
            }

            List<File> viewFiles = scriptFiles.findAll {
//                (it.name.startsWith("data") || it.name.startsWith("view")) && it.name.endsWith(".sql")
                (it.name.startsWith("view")) && it.name.endsWith(".sql")
            }
            if (viewFiles) {
                for (File viewFile : viewFiles) {
                    println("applying data/view script: " + viewFile.name + "\t\t>\tplugin - " + pluginName)
                    try {
                        sql.withTransaction {
                            sql.execute(viewFile.getText("UTF-8"))
                        }
                    } catch (Exception ex) {
                        println("failed to apply data/view script: " + viewFile.name + "\t\t>\tplugin - " + pluginName)
                        println("rolling back data/view script: " + viewFile.name + "\t\t>\tplugin - " + pluginName)
                        throw ex
                    }
                }
            }

        }
    }

    private long getLastVersion(Sql sql, Long pluginId) {
        Long maxVersion = 0
        try {
            String versionSQL = "SELECT COALESCE(MAX(version_number), 0) AS max_count FROM `core_version` WHERE plugin_id = " + pluginId
            maxVersion = sql.firstRow(versionSQL).max_count as Long
        } catch (Exception ignored) {
        }
        return maxVersion
    }
}