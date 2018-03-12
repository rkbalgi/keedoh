package com.daalitoy.apps.keedoh.system;

import com.daalitoy.apps.keedoh.data.model.Spec;
import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class JsonSpecManager implements SpecManager {

    private static final Logger log = LogManager.getLogger(JsonSpecManager.class);
    private final ObjectMapper mapper = new ObjectMapper();
    List<Spec> allSpecs = Lists.newArrayList();

    public JsonSpecManager() {

        List<File> specFiles =
                Arrays.asList(
                        Paths.get(System.getProperty(KeedohConstants.KEEDOH_CONFIG_DIR), "specs").toFile().listFiles());
        try {

            specFiles.forEach(
                    specFile -> {
                        // ValueNode node = (ValueNode) jsonNode;
                        log.info(() -> "processing spec definition file - " + specFile.getName());
                        JsonNode specDef = null;
                        try {
                            specDef = mapper.readTree(Files.toByteArray(specFile));
                            // do some internal object wiring
                            Spec spec = mapper.convertValue(specDef, Spec.class);

                            spec.getMessages().forEach(m -> m.setSpec(spec));

                            allSpecs.add(spec);

                            if (log.isDebugEnabled()) {

                                StringBuilder builder = new StringBuilder();
                                builder.append("spec: " + spec.getSpecName() + "\n");

                                spec.getMessages().forEach(m -> builder.append(m.dumpToString()));
                                log.debug(builder.toString());
                            }
                        } catch (IOException e) {
                            log.error("Failed to read spec definitions", e);
                            throw new RuntimeException(e);
                        }
                    });
        } catch (Exception e) {
            log.error("Failed to read spec definitions", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public Spec newSpec(String specName) {
        return null;
    }

    @Override
    public List<Spec> allSpecs() {
        return allSpecs;
    }
}
