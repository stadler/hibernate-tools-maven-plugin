# hibernate-tools-maven-plugin
Maven Plugin to generate JPA Entities from an existing database using hibernate-tools.

## Development status
Currently only database reverse engineering configurations (JDBCMetaDataConfiguration) with JPA Entity export (hbm2java) is supported.

# Documentation
https://stadler.github.io/hibernate-tools-maven-plugin/

## Hibernate Tools Documentation
For details about the usage of hibernate-tools see:
https://docs.jboss.org/tools/latest/en/hibernatetools/html_single/

## Maven Plugin Usage
An example project using this plugin can be found here:
https://github.com/stadler/hibernate-tools-maven-plugin-sample

The plugin declaration may look as follows:
```
    <plugin>
        <groupId>com.github.stadler</groupId>
        <artifactId>hibernate-tools-maven-plugin</artifactId>
        <version>${hibernate-tools-maven-plugin.version}</version>
        <executions>
            <execution>
                <id>Display Help</id>
                <phase>validate</phase>
                <goals>
                    <goal>help</goal>
                </goals>
            </execution>
            <execution>
                <id>Entity generation</id>
                <phase>generate-sources</phase>
                <goals>
                    <goal>hbm2java</goal>
                </goals>
                <configuration>
                    <templatePath>${project.basedir}/src/main/resources/templates/</templatePath>
                    <!-- Defaults: -->
                    <outputDirectory>${project.build.directory}/generated-sources/</outputDirectory>
                    <ejb3>false</ejb3>
                    <jdk5>false</jdk5>
                </configuration>
            </execution>
            <execution>
                <id>Schema generation</id>
                <phase>generate-resources</phase>
                <goals>
                    <goal>hbm2ddl</goal>
                </goals>
                <configuration>
                    <!--Possible targetType: SCRIPT (default), STDOUT, DATABASE-->
                    <targetTypes>
                        <param>SCRIPT</param>
                        <param>STDOUT</param>
                        <param>DATABASE</param>
                    </targetTypes>
                    <!-- Defaults:-->
                    <outputDirectory>${project.build.directory}/generated-resources/</outputDirectory>
                     <!--Possible schemaExportAction: CREATE (default), DROP, BOTH-->
                    <schemaExportAction>CREATE</schemaExportAction>
                    <outputFileName>schema.ddl</outputFileName>
                    <delimiter>;</delimiter>
                    <haltOnError>true</haltOnError>
                    <format>true</format>
                </configuration>
            </execution>
        </executions>
        <configuration>
            <revengFile>${project.basedir}/src/main/hibernate/hibernate.reveng.xml</revengFile>
            <!-- Defaults:-->
            <packageName></packageName>
            <configFile>${project.basedir}/src/main/hibernate/hibernate.cfg.xml</configFile>
            <detectManyToMany>true</detectManyToMany>
            <detectOneToOne>true</detectOneToOne>
            <detectOptimisticLock>true</detectOptimisticLock>
            <createCollectionForForeignKey>true</createCollectionForForeignKey>
            <createManyToOneForForeignKey>true</createManyToOneForForeignKey>
        </configuration>
        <dependencies>
            <dependency>
                <!-- DB Driver of your choice -->
                <groupId>com.h2database</groupId>
                <artifactId>h2</artifactId>
                <version>${h2.version}</version>
            </dependency>
        </dependencies>
    </plugin>
</plugins>
```
