package com.github.stadler;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.hibernate.cfg.JDBCMetaDataConfiguration;
import org.hibernate.cfg.reveng.DefaultReverseEngineeringStrategy;
import org.hibernate.cfg.reveng.OverrideRepository;
import org.hibernate.cfg.reveng.ReverseEngineeringSettings;
import org.hibernate.cfg.reveng.ReverseEngineeringStrategy;
import org.hibernate.tool.hbm2x.POJOExporter;

import java.io.File;

import static org.apache.maven.plugins.annotations.LifecyclePhase.GENERATE_SOURCES;

/**
 * Mojo to generate Java JPA Entities from an existing database.
 */
@Mojo(name = "hbm2java", defaultPhase = GENERATE_SOURCES)
public class Hbm2JavaMojo extends AbstractMojo {

    @Parameter
    private String packageName;
    @Parameter(defaultValue = "${project.build.directory}/generated-sources/")
    private File outputDirectory;
    @Parameter(defaultValue = "${project.basedir}/src/main/hibernate/hibernate.cfg.xml")
    private File configFile;
    @Parameter
    private File revengFile;
    @Parameter(defaultValue = "true")
    private boolean detectManyToMany;
    @Parameter(defaultValue = "true")
    private boolean detectOneToOne;
    @Parameter(defaultValue = "true")
    private boolean detectOptimisticLock;
    @Parameter(defaultValue = "true")
    private boolean createCollectionForForeignKey;
    @Parameter(defaultValue = "true")
    private boolean createManyToOneForForeignKey;


    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Starting " + this.getClass().getSimpleName() + "...");

        ReverseEngineeringStrategy strategy = new DefaultReverseEngineeringStrategy();

        if (revengFile != null) {
            OverrideRepository override = new OverrideRepository();
            override.addFile(revengFile);
            strategy = override.getReverseEngineeringStrategy(strategy);
        }

        ReverseEngineeringSettings settings =
                new ReverseEngineeringSettings(strategy)
                        .setDefaultPackageName(packageName)
                        .setDetectManyToMany(detectManyToMany)
                        .setDetectOneToOne(detectOneToOne)
                        .setDetectOptimisticLock(detectOptimisticLock)
                        .setCreateCollectionForForeignKey(createCollectionForForeignKey)
                        .setCreateManyToOneForForeignKey(createManyToOneForForeignKey);

        strategy.setSettings(settings);

        JDBCMetaDataConfiguration cfg = new JDBCMetaDataConfiguration();
        cfg.setReverseEngineeringStrategy(strategy);

        getLog().info("Configuring using file: " + configFile + "...");
        cfg.configure(configFile);

        getLog().info("Reading from JDBC...");
        cfg.readFromJDBC();

        POJOExporter pojoExporter = new POJOExporter(cfg, outputDirectory);
        getLog().info("Starting POJO export to directory: " + outputDirectory + "...");
        pojoExporter.start();
        getLog().info("Finished " + this.getClass().getSimpleName() + "!");
    }
}
