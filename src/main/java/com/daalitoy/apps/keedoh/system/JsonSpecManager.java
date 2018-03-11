package com.daalitoy.apps.keedoh.system;

import com.daalitoy.apps.keedoh.data.model.Spec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ValueNode;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class JsonSpecManager implements SpecManager {

    private static final Logger log = LogManager.getLogger(JsonSpecManager.class);
    private final ObjectMapper mapper = new ObjectMapper();
    List<Spec> allSpecs = Lists.newArrayList();

    public JsonSpecManager() {

        URL specListUrl = Resources.getResource("keedoh-specs.json");
        try {
            ArrayNode specList = (ArrayNode) mapper.readTree((specListUrl));

            specList
                    .elements()
                    .forEachRemaining(
                            jsonNode -> {
                                ValueNode node = (ValueNode) jsonNode;
                                log.info(() -> "processing spec definition file - " + node.textValue());
                                String fileName = node.textValue();
                                JsonNode specDef = null;
                                try {
                                    specDef = mapper.readTree(Resources.getResource(fileName));
                                    // do some internal object wiring
                                    Spec spec = mapper.convertValue(specDef, Spec.class);

                                    spec.getMessages().forEach(m -> m.setSpec(spec));
                                    StringBuilder builder = new StringBuilder();
                                    builder.append("spec: " + spec.getSpecName() + "\n");

                                    spec.getMessages()
                                            .forEach(
                                                    m -> {
                                                        builder.append(m.dumpToString());
                                                    });
                                    log.info(builder.toString());
                                    allSpecs.add(spec);
                                } catch (IOException e) {
                                    log.error("Failed to read spec definitions", e);
                                    throw new RuntimeException(e);
                                }
                            });
        } catch (IOException e) {
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
