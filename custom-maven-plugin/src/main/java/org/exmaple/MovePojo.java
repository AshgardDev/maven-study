package org.exmaple;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Build;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * @Description: 移动jar包到指定的绝对路径
 * @Author: hbj
 * @Email: huangbinjie2024@gmail.com
 * @Date:2024/12/26 11:05
 */
@Mojo(name = "move", defaultPhase = LifecyclePhase.PACKAGE)
public class MovePojo extends AbstractMojo {

    @Parameter(defaultValue = "${project}", readonly = true)
    private MavenProject project;

    @Parameter(name = "toAbsoluteDir", required = true)
    private String toAbsoluteDir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        Log log = getLog();
        Build build = project.getBuild();
        String directory = build.getDirectory();
        String artifactId = project.getArtifact().getArtifactId();
        String packaging = project.getPackaging();
        String finalName = build.getFinalName();

        log.info("directory: " + directory);
        log.info("artifactId: " + artifactId);
        log.info("packaging: " + packaging);
        log.info("finalName: " + finalName);

        if (StringUtils.isNotEmpty(toAbsoluteDir)) {
            log.info("toAbsoluteDir ==> " + toAbsoluteDir);
        }

        File jarFile = new File(directory + File.separator + finalName + "." + packaging);
        if (jarFile.exists()) {
            log.info("jarFile:" + jarFile);
        }
        try {
            File toAbsoluteFile = new File(this.toAbsoluteDir + File.separator + finalName + "." + packaging);
            Path path = Files.copy(jarFile.toPath(), toAbsoluteFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            log.info("拷贝文件成功：" + path);
        } catch (IOException e) {
            log.error("拷贝文件失败", e);
        }
    }
}
