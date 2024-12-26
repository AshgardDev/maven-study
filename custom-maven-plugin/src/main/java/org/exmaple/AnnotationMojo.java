package org.exmaple;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

/**
 * 注解方式插件
 * @Description:
 * @Author: hbj
 * @Email: huangbinjie2024@gmail.com
 * @Date:2024/12/26 10:30
 */
@Mojo(name = "annotation", defaultPhase = LifecyclePhase.PACKAGE)
public class AnnotationMojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(property = "name", required = true)
    private String name;

    @Parameter(property = "description")
    private String description;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        log.info("注解获取到project信息:" + project);
        log.info("获取到配置信息name:" + name);
        log.info("获取到配置信息description:" + description);
    }
}
