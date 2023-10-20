package ru.vasiand.spring.boot.log4jdbc;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;

import java.sql.Driver;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Properties for configuring log4jdbc spy.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "log4jdbc")
public class Log4jdbcProperties {
    static final String[] PROPERTIES = {
        "log4jdbc.auto.load.popular.drivers",
        "log4jdbc.debug.stack.prefix",
        "log4jdbc.drivers",
        "log4jdbc.dump.booleanastruefalse",
        "log4jdbc.dump.fulldebugstacktrace",
        "log4jdbc.dump.sql.addsemicolon",
        "log4jdbc.dump.sql.create",
        "log4jdbc.dump.sql.delete",
        "log4jdbc.dump.sql.insert",
        "log4jdbc.dump.sql.maxlinelength",
        "log4jdbc.dump.sql.select",
        "log4jdbc.dump.sql.update",
        "log4jdbc.log4j2.properties.file",
        "log4jdbc.sqltiming.error.threshold",
        "log4jdbc.sqltiming.warn.threshold",
        "log4jdbc.statement.warn",
        "log4jdbc.suppress.generated.keys.exception",
        "log4jdbc.trim.sql",
        "log4jdbc.trim.sql.extrablanklines",
    };

    private Auto auto = new Auto();

    private Debug debug = new Debug();

    /**
     * One or more fully qualified class names for JDBC drivers that log4jdbc should load and wrap. This option is not
     * normally needed because most popular JDBC drivers are already loaded by default. This should be used if one or
     * more additional JDBC drivers that (log4jdbc doesn't already wrap) needs to be included.
     */
    private List<Class<? extends Driver>> drivers;

    private Dump dump = new Dump();

    private Log4j2 log4j2 = new Log4j2();

    private SqlTiming sqltiming = new SqlTiming();

    private Statement statement = new Statement();

    private Suppress suppress = new Suppress();

    private Trim trim = new Trim();

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "log4jdbc.auto")
    public static class Auto {

        private Load load = new Load();

        @Getter
        @Setter
        @ConfigurationProperties(prefix = "log4jdbc.auto.load")
        public static class Load {

            private Popular popular = new Popular();

            @Getter
            @Setter
            @ConfigurationProperties(prefix = "log4jdbc.auto.load.popular")
            public static class Popular {

                /**
                 * Automatically load popular drivers. If this is false, you must set the log4jdbc.drivers property in
                 * order to load the driver(s) you want.
                 */
                private boolean drivers = true;
            }
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "log4jdbc.debug")
    public static class Debug {

        private Stack stack = new Stack();

        @Getter
        @Setter
        @ConfigurationProperties(prefix = "log4jdbc.debug.stack")
        public static class Stack {

            /**
             * A REGEX matching the package name of your application. The call stack will be searched down to the first
             * occurrence of a class that has the matching REGEX. If this is not set, the actual class that called into
             * log4jdbc is used in the debug output (in many cases this will be a connection pool class). For example,
             * setting a system property such as this: -Dlog4jdbc.debug.stack.prefix=^com.mycompany.myapp.* would cause
             * the call stack to be searched for the first call that came from code in the com.mycompany.myapp package
             * or below, thus if all of your sql generating code was in code located in the com.mycompany.myapp package
             * or any subpackages, this would be printed in the debug information, rather than the package name for a
             * connection pool, object relational system, etc. Please note that the behavior of this property has
             * changed as compared to the standard log4jdbc implementation. This property is now a REGEX, instead of
             * being just the package prefix of the stack trace. So, for instance, if you want to target the prefix
             * org.mypackage, the value of this property should be: ^org.mypackage.*.
             */
            private Pattern prefix;
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "log4jdbc.dump")
    public static class Dump {

        /**
         * When dumping boolean values in SQL, dump them as 'true' or 'false'. If this option is not set, they will be
         * dumped as 1 or 0 as many databases do not have a boolean type, and this allows for more portable sql dumping.
         */
        private boolean booleanastruefalse = false;

        /**
         * If dumping in debug mode, dump the full stack trace. This will result in EXTREMELY voluminous output, but
         * can be very useful under some circumstances when trying to track down the call chain for generated SQL.
         */
        private boolean fulldebugstacktrace = false;

        private Sql sql = new Sql();

        @Getter
        @Setter
        @ConfigurationProperties(prefix = "log4jdbc.dump.sql")
        public static class Sql {

            /**
             * Set this to true to add an extra semicolon to the end of SQL in the output. This can be useful when you
             * want to generate SQL from a program with log4jdbc in order to create a script to feed back into a
             * database to run at a later time.
             */
            private boolean addsemicolon = false;

            /**
             * Set this to false to suppress SQL create statements in the output. Note that if you use the Log4j 2
             * logger, it is also possible to control create statements output via the marker LOG4JDBC_CREATE. The
             * use of this property prepend the use of the marker.
             */
            private boolean create = true;

            /**
             * Set this to false to suppress SQL delete statements in the output. Note that if you use the Log4j 2
             * logger, it is also possible to control delete statements output via the marker LOG4JDBC_DELETE. The use
             * of this property prepend the use of the marker.
             */
            private boolean delete = true;

            /**
             * Set this to false to suppress SQL insert statements in the output. Note that if you use the Log4j 2
             * logger, it is also possible to control insert statements output via the marker. The use of this property
             * prepend the use of the marker.
             */
            private boolean insert = true;

            /**
             * When dumping SQL, if this is greater than 0, than the dumped SQL will be broken up into lines that are
             * no longer than this value. Set this value to 0 if you don't want log4jdbc to try and break the SQL into
             * lines this way. In future versions of log4jdbc, this will probably default to 0.
             */
            private int maxlinelength = 90;

            /**
             * Set this to false to suppress SQL select statements in the output. Note that if you use the Log4j 2
             * logger, it is also possible to control select statements output via the marker LOG4JDBC_SELECT. The use
             * of this property prepend the use of the marker.
             */
            private boolean select = true;

            /**
             * Set this to false to suppress SQL update statements in the output. Note that if you use the Log4j 2
             * logger, it is also possible to control update statements output via the marker LOG4JDBC_UPDATE. The use
             * of this property prepend the use of the marker.
             */
            private boolean update = true;
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "log4jdbc.log4j2")
    public static class Log4j2 {

        private Properties properties = new Properties();

        @Getter
        @Setter
        @ConfigurationProperties(prefix = "log4jdbc.log4j2.properties")
        public static class Properties {

            /**
             * Name of the property file to use
             */
            private Resource file;
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "log4jdbc.sqltiming")
    public static class SqlTiming {

        private Error error = new Error();

        private Warn warn = new Warn();

        @Getter
        @Setter
        @ConfigurationProperties(prefix = "log4jdbc.sqltiming.error")
        public static class Error {

            /**
             * Millisecond time value. Causes SQL that takes the number of milliseconds specified or more time to
             * execute to be logged at the error level in the sqltiming log. Note that the sqltiming log must be enabled
             * at the error log level for this feature to work. Also the logged output for this setting will log with
             * debug information that is normally only shown when the sqltiming log is enabled at the debug level. This
             * can help you to more quickly find slower running SQL without adding overhead or logging for normal
             * running SQL that executes below the threshold level (if the logging level is set appropriately.)
             */
            private Long threshold;
        }

        @Getter
        @Setter
        @ConfigurationProperties(prefix = "log4jdbc.sqltiming.warn")
        public static class Warn {

            /**
             * Millisecond time value. Causes SQL that takes the number of milliseconds specified or more time to
             * execute to be logged at the warning level in the sqltiming log. Note that the sqltiming log must be
             * enabled at the warn log level for this feature to work. Also the logged output for this setting will log
             * with debug information that is normally only shown when the sqltiming log is enabled at the debug level.
             * This can help you to more quickly find slower running SQL without adding overhead or logging for normal
             * running SQL that executes below the threshold level (if the logging level is set appropriately).
             */
            private Long threshold;
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "log4jdbc.statement")
    public static class Statement {

        /**
         * Set this to true to display warnings (Why would you care?) in the log when Statements are used in the log.
         */
        private boolean warn = false;
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "log4jdbc.suppress")
    public static class Suppress {

        private Generated generated = new Generated();

        @Getter
        @Setter
        @ConfigurationProperties(prefix = "log4jdbc.suppress.generated")
        public static class Generated {

            private Keys keys = new Keys();

            @Getter
            @Setter
            @ConfigurationProperties(prefix = "log4jdbc.suppress.generated.keys")
            public static class Keys {

                /**
                 * Set to true to ignore any exception produced by the method, Statement.getGeneratedKeys()
                 */
                private boolean exception = false;
            }
        }
    }

    @Getter
    @Setter
    @ConfigurationProperties(prefix = "log4jdbc.trim")
    public static class Trim {

        /**
         * Set this to false to not trim the logged SQL.
         */
        private boolean sql = true;

        @Getter
        @Setter
        @ConfigurationProperties(prefix = "log4jdbc.trim.sql")
        public static class Sql {

            /**
             * Set this to false to not trim extra blank lines in the logged SQL (by default, when more than one blank
             * line in a row occurs, the contiguous lines are collapsed to just one blank line.)
             */
            private boolean extrablanklines = true;
        }
    }
}
