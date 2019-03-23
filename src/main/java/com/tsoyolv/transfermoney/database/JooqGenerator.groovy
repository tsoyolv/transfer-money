package com.tsoyolv.transfermoney.database

import com.tsoyolv.transfermoney.ApplicationProperties
import com.tsoyolv.transfermoney.LoggerWrapper
import groovy.xml.MarkupBuilder
import org.jooq.codegen.GenerationTool

import static com.tsoyolv.transfermoney.ApplicationProperties.DB_CONNECTION_URL_PROPERTY_NAME
import static com.tsoyolv.transfermoney.ApplicationProperties.DB_DRIVER_PROPERTY_NAME
import static com.tsoyolv.transfermoney.ApplicationProperties.DB_PASSWORD_PROPERTY_NAME
import static com.tsoyolv.transfermoney.ApplicationProperties.DB_USER_PROPERTY_NAME
import static com.tsoyolv.transfermoney.LoggerWrapper.JDBC_LOGGER_NAME


/**
 * todo must be called from Application, or as gradle task
 */
class JooqGenerator {
    static final String dbDriver = ApplicationProperties.getProperty(DB_DRIVER_PROPERTY_NAME)
    static final String connectionUrl = ApplicationProperties.getProperty(DB_CONNECTION_URL_PROPERTY_NAME)
    static final String userName = ApplicationProperties.getProperty(DB_USER_PROPERTY_NAME)
    static final String passwords = ApplicationProperties.getProperty(DB_PASSWORD_PROPERTY_NAME)

    static final LoggerWrapper log = LoggerWrapper.getLogger(JDBC_LOGGER_NAME)

    static void main(String[] args) {
        generate()
    }

    static void generate() {
        log.debug("Jooq generation start")
        def xml = createXmlConfiguration()
        GenerationTool.generate(xml)
        log.debug("Jooq configuration: \r\n, {}", xml)
        log.debug("Jooq generation Finish")
    }

    static String createXmlConfiguration() {
        def xml = new StringWriter()
        new MarkupBuilder(xml)
                .configuration('xmlns': 'http://www.jooq.org/xsd/jooq-codegen-3.11.0.xsd') {
            jdbc() {
                driver(dbDriver)
                url(connectionUrl)
                user(userName)
                password(passwords)
            }
            generator() {
                database() {
                    inputSchema('PUBLIC')
                    includes('.*')
                    excludes()
                }

                // Watch out for this caveat when using MarkupBuilder with "reserved names"
                // - https://github.com/jOOQ/jOOQ/issues/4797
                // - http://stackoverflow.com/a/11389034/521799
                // - https://groups.google.com/forum/#!topic/jooq-user/wi4S9rRxk4A
                generate([:]) {
                    pojos true
                    daos true
                }
                target() {
                    packageName('com.tsoyolv.transfermoney.generated.jooq')
                    directory('src/main/java')
                }
            }
        }
        return xml.toString()
    }
}
