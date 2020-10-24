package com.grailslab.core

import com.grailslab.SequenceConstructor
import com.grailslab.common.SequenceConfig
import grails.gorm.transactions.Transactional

@Transactional
class SequenceGeneratorService extends BaseService{

    /**
     * generate sequence evaluating the sequence script configured
     * @param sequenceName the configured unique sequence name
     * @param persist whether to persist generated sequential value to database
     * @return
     */
    synchronized String generate(String sequenceName, Boolean persist = true) {
        return generateTenant(sequenceName, null, null, persist)
    }

    /**
     * generate sequence evaluating the sequence script configured and argument map
     * @param sequenceName the configured unique sequence name
     * @param args arguments to pass to the configured sequenceScript
     * @param persist whether to persist generated sequential value to database
     * @return
     */
    synchronized String generate(String sequenceName, Map args, Boolean persist = true) {
        return generateTenant(sequenceName, null, args, persist)
    }

    /**
     * generate sequence for tenant evaluating the sequence script configured
     * @param sequenceName the configured unique sequence name
     * @param tenantId for each tenant there will be individual sequenceScript in database
     * @param persist whether to persist generated sequential value to database
     * @return
     */
    synchronized String generateTenant(String sequenceName, Long tenantId, Boolean persist = true) {
        return generateTenant(sequenceName, tenantId, null, persist)
    }

    /**
     * generate sequence for tenant evaluating the sequence script configured and argument map
     * @param sequenceName the configured unique sequence name
     * @param tenantId for each tenant there will be individual sequenceScript in database
     * @param args arguments to pass to the configured sequenceScript
     * @param persist whether to persist generated sequential value to database
     * @return
     */
    synchronized String generateTenant(String sequenceName, Long tenantId, Map args, Boolean persist = true) {
        if (args == null) args = [:]
        SequenceConfig sequenceConfig = SequenceConfig.createCriteria().get {
            eq("sequenceName", sequenceName)
            if (tenantId != null) {
                eq("tenantId", tenantId)
            } else {
                isNull("tenantId")
            }
        } as SequenceConfig
        if (!sequenceConfig && tenantId == null) {
            throw new RuntimeException("Sequence configuration :${sequenceName} not found in the configuration list")
        } else if (!sequenceConfig) {
            SequenceConstructor.SequenceConfig config = SequenceConstructor.sequenceConfigList.find {
                it.sequenceName == sequenceName
            }
            sequenceConfig = new SequenceConfig()
            sequenceConfig.sequenceName = config.sequenceName
            sequenceConfig.sequenceScript = config.sequenceScript
            sequenceConfig.sequenceNumber = config.sequenceNumber
            sequenceConfig.tenantId = tenantId
            sequenceConfig.save()
        }

        if (persist && sequenceConfig.sequenceNumber != null) {
            args.sequenceNumber = sequenceConfig.sequenceNumber
            sequenceConfig.sequenceNumber++
            sequenceConfig.save()
        } else if (sequenceConfig.sequenceNumber != null) {
            args.sequenceNumber = sequenceConfig.sequenceNumber
        }
        return evaluateSequenceScript(sequenceConfig.sequenceScript, args)
    }

    /**
     * evaluate the groovy script
     * @param sequenceScript
     * @param args
     * @return
     */
    private static String evaluateSequenceScript(String sequenceScript, Map args) {
        Binding scriptArguments = new Binding()
        GroovyShell shell = new GroovyShell(scriptArguments)
        args.each { key, value ->
            scriptArguments.setProperty(key.toString(), value)
        }
        return shell.evaluate(sequenceScript)
    }

    /**
     * this method creates all sequence configs in database if doesn't exist with sequenceName
     */
    void initialize() {
        List<SequenceConfig> dbSequenceConfigList = SequenceConfig.findAllByTenantIdIsNull(READ_ONLY)
        for (SequenceConstructor.SequenceConfig config : SequenceConstructor.sequenceConfigList) {
            SequenceConfig dbSequenceConfig = dbSequenceConfigList.find { it.sequenceName = config.sequenceName }
            if (!dbSequenceConfig) {
                SequenceConfig sequenceConfig = new SequenceConfig()
                sequenceConfig.sequenceName = config.sequenceName
                sequenceConfig.sequenceScript = config.sequenceScript
                sequenceConfig.sequenceNumber = config.sequenceNumber
                sequenceConfig.save()
            }
        }
    }
}
