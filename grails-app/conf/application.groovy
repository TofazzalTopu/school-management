grails.profile = "web"
grails.codegen.defaultPackage = "myschool"
grails.spring.transactionManagement.proxies = false
//hibernate.flush.mode = AUTO
grails.gorm.autowire = false     // Whether to autowire entities. Disabled by default for performance reasons.
grails.gorm.reactor.events = false //Whether to translate GORM events into Reactor events. Disabled by default for performance reasons
grails.gorm.failOnError = true // will raise exception on gorm validation error
grails.cors.enabled = true
//grails.cors.allowedOrigins = ['http://localhost:4200']
//grails.cors.allowedOrigins = ['http://www.amaruddog.com']
environments {
	development {
		server.contextPath='/myschool'
	}
	test {
		server.contextPath='/'
	}
	production {
		//server.contextPath='/'
	}
}
server.port = 8083
info.app.name = '@info.app.name@'
info.app.version = '@info.app.version@'
info.app.grailsVersion = '@info.app.grailsVersion@'

spring.main['banner-mode'] = "off"
sping.groovy.template['check-template-location'] = false

endpoints.enabled = false
endpoints.jmx.enabled = true
endpoints.jmx['unique-names'] = true

//grails.mime.use.accept.header = true
grails.mime.disable.accept.header.userAgents = ['Gecko', 'WebKit', 'Presto', 'Trident']
grails.mime.types = [
		all          : '*/*',
		atom         : 'application/atom+xml',
		css          : 'text/css',
		csv          : 'text/csv',
		form         : 'application/x-www-form-urlencoded',
		html         : ['text/html', 'application/xhtml+xml'],
		js           : 'text/javascript',
		json         : ['application/json', 'text/json'],
		multipartForm: 'multipart/form-data',
		rss          : 'application/rss+xml',
		text         : 'text/plain',
		hal          : ['application/hal+json', 'application/hal+xml'],
		xml          : ['text/xml', 'application/xml'],
		pdf          : 'application/pdf',
		zip          : 'application/zip'
]

grails.urlmapping.cache.maxsize = 1000
grails.controllers.defaultScope = 'singleton'
grails.controllers.upload.maxFileSize = 20971520  // 20MB
grails.controllers.upload.maxRequestSize = 20971520  // 20MB
grails.converters.encoding = "UTF-8"
grails.converters.json.domain.include.version = true

//converts string sent from client side and bind it to date object
grails.databinding.dateFormats = ['dd/MM/yyyy', 'dd-MM-yyyy']

grails {
	views {
		'default' {
			codec = 'html'
		}
		gsp {
			encoding = 'UTF-8'
			htmlcodec = 'xml'
			codecs {
				expression = 'none'
				scriptlets = 'html'
				taglib = 'none'
				staticparts = 'none'
			}
		}
	}
}

/**
 * inactive session timeout (30 minutes)
 */
server.session.timeout = 1800

/**
 * will raise exception on gorm validation error
 */
grails.gorm.failOnError = true

/**
 ********************************************************************
 * hibernate config
 ********************************************************************
 */
hibernate {
	cache {
		queries = false
		use_second_level_cache = true
		use_query_cache = true
		region['factory_class'] = 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory'
	}
}
/**
 ********************************************************************
 * Datasource config
 ********************************************************************
 */
dataSource {
	pooled = true
	jmxExport = true
//	dbCreate = 'validate'
	dbCreate = 'update'
	driverClassName = 'com.mysql.jdbc.Driver'
	dialect = 'com.grailslab.myapp.domain.MySQLUTF8InnoDBDialect'
	//noinspection GroovyAssignabilityCheck
	properties {
		jmxEnabled = true
		maxActive = 50
		maxIdle = 25
		minIdle = 5
		initialSize = 5
		minEvictableIdleTimeMillis = 60000
		timeBetweenEvictionRunsMillis = 5000
		maxWait = 10000
		maxAge = 10 * 60000
		numTestsPerEvictionRun = 3
		testOnBorrow = true
		testWhileIdle = true
		testOnReturn = false
		ignoreExceptionOnPreLoad = true
		validationQuery = "SELECT 1"
		validationQueryTimeout = 3
		jdbcInterceptors = "ConnectionState;StatementCache(max=200)"
		defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
		abandonWhenPercentageFull = 100
		removeAbandonedTimeout = 120
		removeAbandoned = true
		logAbandoned = false
		dbProperties {
			// Mysql specific driver properties
			// http://dev.mysql.com/doc/connector-j/en/connector-j-reference-configuration-properties.html

			autoReconnect = true
			jdbcCompliantTruncation = false
			zeroDateTimeBehavior = 'convertToNull'
//            cachePrepStmts = false                    // Tomcat JDBC Pool's StatementCache is used instead, so disable mysql driver's cache
//            cacheCallableStmts = false
//            dontTrackOpenResources = true             // Tomcat JDBC Pool's StatementFinalizer keeps track
//            holdResultsOpenOverStatementClose = true  // performance optimization: reduce number of SQLExceptions thrown in mysql driver code
//            useServerPrepStmts = false                // enable MySQL query cache - using server prep stmts will disable query caching
//            cacheServerConfiguration = true           // metadata caching
//            cacheResultSetMetadata = true
//            metadataCacheSize = 100
//            connectTimeout = 15000                    // timeouts for TCP/IP
//            socketTimeout = 120000
//            maintainTimeStats = false                 // timer tuning (disable)
//            enableQueryTimeouts = false
//            noDatetimeStringSync = true               // misc tuning
		}
	}
}

/**
 ********************************************************************
 * Environment specific config
 ********************************************************************
 */

powersms.api.userid = "grailslab"
powersms.api.password = "grailslab123"
mysms.api.userid = "aminulgl"
mysms.api.password = "@V'n}h[uN"

environments {
	development {
		grails.config.locations = [
				"classpath:config/dev-config.properties",
		]
	}
	test {
		grails.config.locations = [
				"classpath:config/test-config.properties",
		]
	}
	production {
		grails.config.locations = [
				"classpath:config/prod-config.properties",
		]
	}
}


// Added by the Spring Security Core plugin:
grails.plugin.springsecurity.rejectIfNoRule = true                             // reject if rule not defined in request_map
//grails.plugin.springsecurity.fii.rejectPublicInvocations = false                 // reject public url invocation

grails.plugin.springsecurity.apf.storeLastUsername = true
grails.plugin.springsecurity.apf.usernameParameter = "j_username"                //login form username parameter
grails.plugin.springsecurity.apf.passwordParameter = "j_password"                //login form password parameter
grails.plugin.springsecurity.apf.postOnly = true                                //login form submission post only
grails.plugin.springsecurity.logout.postOnly = false                            //logout post only
grails.plugin.springsecurity.successHandler.alwaysUseDefault = true             //if true, always redirects to the value of successHandler
grails.plugin.springsecurity.successHandler.defaultTargetUrl = '/login/loginSuccess'    //redirect url after login success
grails.plugin.springsecurity.useSecurityEventListener = true
grails.plugin.springsecurity.adh.errorPage = null

grails.plugin.springsecurity.password.hash.iterations = 1

grails.plugin.springsecurity.userLookup.userDomainClassName = 'com.amaruddog.myschool.User'
grails.plugin.springsecurity.userLookup.authorityJoinClassName = 'com.amaruddog.myschool.UserRole'
grails.plugin.springsecurity.authority.className = 'com.amaruddog.myschool.Role'
grails.plugin.springsecurity.controllerAnnotations.staticRules = [
	[pattern: '/',               access: ['permitAll']],
	[pattern: '/error',          access: ['permitAll']],
	[pattern: '/index',          access: ['permitAll']],
	[pattern: '/index.gsp',      access: ['permitAll']],
	[pattern: '/shutdown',       access: ['permitAll']],
	[pattern: '/assets/**',      access: ['permitAll']],
	[pattern: '/**/js/**',       access: ['permitAll']],
	[pattern: '/**/css/**',      access: ['permitAll']],
	[pattern: '/**/images/**',   access: ['permitAll']],
	[pattern: '/**/favicon.ico', access: ['permitAll']]
]

grails.plugin.springsecurity.filterChain.chainMap = [
	[pattern: '/assets/**',      filters: 'none'],
	[pattern: '/**/js/**',       filters: 'none'],
	[pattern: '/**/css/**',      filters: 'none'],
	[pattern: '/**/images/**',   filters: 'none'],
	[pattern: '/**/favicon.ico', filters: 'none'],
	[pattern: '/**',             filters: 'JOINED_FILTERS']
]

grails.plugin.springsecurity.rememberMe.cookieName = 'grailslab_auth_cookie'
grails.plugin.springsecurity.rememberMe.alwaysRemember = false
grails.plugin.springsecurity.rememberMe.tokenValiditySeconds = 604800   //7 days
grails.plugin.springsecurity.rememberMe.key = 'R@y$eCUae3C0oK1#e'

grails.plugin.springsecurity.useSwitchUserFilter = true
grails.plugin.springsecurity.switchUser.switchUserUrl = '/login/impersonate'
grails.plugin.springsecurity.switchUser.exitUserUrl = '/logout/impersonate'
grails.plugin.springsecurity.switchUser.targetUrl = null // use the authenticationSuccessHandler
grails.plugin.springsecurity.switchUser.switchFailureUrl = '/login/switchFailed'

grails.plugin.springsecurity.securityConfigType = "InterceptUrlMap"

grails.plugin.springsecurity.interceptUrlMap = [
		[pattern:'/login/impersonate', 					access: ['ROLE_SWITCH_USER']],
		[pattern:'/logout/impersonate', 				access: ['permitAll']],
	 	[pattern:'/', 									access: ['permitAll']],
		[pattern:'/index', 								access: ['permitAll']],
	   	[pattern:'/assets/**', 							access: ['permitAll']],
		[pattern:'/image/**', 							access: ['permitAll']],
	 	[pattern:'/**/js/**', 							access: ['permitAll']],
		[pattern:'/**/css/**', 							access: ['permitAll']],
		[pattern:'/**/images/**', 						access: ['permitAll']],
	 	[pattern:'/**/favicon.ico', 					access: ['permitAll']],
	 	[pattern:'/login/**', 							access: ['permitAll']],
	 	[pattern:'/logout/**', 							access: ['permitAll']],

//        public url
	 	[pattern:'/home/**', 							access: ['permitAll']],
	 	[pattern:'/online/**', 							access: ['permitAll']],
	 	[pattern:'/reunion/**', 						access: ['permitAll']],
	 	[pattern:'/results/**', 						access: ['permitAll']],
	 	[pattern:'/gallery/**', 						access: ['permitAll']],
	 	[pattern:'/blog/**', 							access: ['permitAll']],
	 	[pattern:'/faq/**', 							access: ['permitAll']],
	 	[pattern:'/calendar/**', 						access: ['permitAll']],
	 	[pattern:'/olympiad/**', 						access: ['permitAll']],

	 	[pattern:'/remote/**', 							access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_ACCOUNTS','ROLE_HR','ROLE_ORGANIZER','ROLE_TEACHER','ROLE_LIBRARY']],
	 	[pattern:'/profile/**', 						access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_ACCOUNTS','ROLE_HR','ROLE_ORGANIZER','ROLE_TEACHER','ROLE_LIBRARY']],
	 	[pattern:'/attendance/**', 						access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_ACCOUNTS', 'ROLE_HR','ROLE_ORGANIZER','ROLE_TEACHER', 'ROLE_LIBRARY']],
	 	[pattern:'/leave/**', 							access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_ACCOUNTS', 'ROLE_HR','ROLE_ORGANIZER','ROLE_TEACHER', 'ROLE_LIBRARY']],
		//all url related to library
	 	[pattern:'/web/**', 							access: ['ROLE_SUPER_ADMIN', 'ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_ORGANIZER']],
	 	[pattern:'/portalSwitch/**', 					access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_HR']],
	 	[pattern:'/portal/**', 							access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_HR','ROLE_STUDENT']],
	 	[pattern:'/sms/**', 							access: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_ACCOUNTS','ROLE_HR']],
	 	[pattern:'/stmgst/**', 							access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_ACCOUNTS','ROLE_HR','ROLE_TEACHER']],
	 	[pattern:'/deviceLog/**', 						access: ['permitAll']],        //device log upload
	 	[pattern:'/attn/**', 							access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_HR','ROLE_TEACHER']],

	 	[pattern:'/admin/**', 							access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE']],
	 	[pattern:'/exam/**', 							access: ['ROLE_SUPER_ADMIN', 'ROLE_ADMIN','ROLE_SHIFT_INCHARGE']],
	 	[pattern:'/evaluation/**', 						access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE']],
	 	[pattern:'/library/**', 						access: ['ROLE_SUPER_ADMIN', 'ROLE_ADMIN','ROLE_LIBRARY']],
	 	[pattern:'/lessonPlan/**', 						access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN', 'ROLE_SHIFT_INCHARGE', 'ROLE_TEACHER']],
	 	[pattern:'/accounts/**', 						access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_ACCOUNTS','ROLE_HR']],
	 	[pattern:'/hrm/**', 							access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_HR']],
	 	[pattern:'/salary/**', 							access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_HR']],
	 	[pattern:'/LeaveMgmt/**', 						access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_HR']],
	 	[pattern:'/teacher/**', 						access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_TEACHER']],
	 	[pattern:'/collection/**', 						access: ['ROLE_SUPER_ADMIN','ROLE_ADMIN','ROLE_ACCOUNTS','ROLE_HR']],
	 	[pattern:'/applicant/**', 						access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_ACCOUNTS']],
	 	[pattern:'/onlineLibrary/**', 					access: ['ROLE_SUPER_ADMIN','ROLE_SCHOOL_HEAD','ROLE_ADMIN','ROLE_SHIFT_INCHARGE','ROLE_LIBRARY']],
	 	[pattern:'/onlineLibrary', 						access: ['permitAll']],
	 	[pattern:'/offlineLibrary', 					access: ['permitAll']]

]