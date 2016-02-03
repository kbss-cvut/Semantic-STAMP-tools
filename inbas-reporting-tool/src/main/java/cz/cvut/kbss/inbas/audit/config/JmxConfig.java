package cz.cvut.kbss.inbas.audit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableMBeanExport;
import org.springframework.jmx.export.MBeanExporter;

import javax.management.MBeanServer;
import java.lang.management.ManagementFactory;

@Configuration
@EnableMBeanExport
@ComponentScan(basePackages = "cz.cvut.kbss.inbas.audit.jmx")
public class JmxConfig {

    @Bean
    public MBeanExporter exporter(MBeanServer server) {
        final MBeanExporter exporter = new MBeanExporter();
        exporter.setAutodetect(true);
        exporter.setServer(server);
        return exporter;
    }

    @Bean
    public MBeanServer mbeanServer() {
        return ManagementFactory.getPlatformMBeanServer();
    }
}
