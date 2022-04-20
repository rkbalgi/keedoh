package com.daalitoy.apps.keedoh.system;

import com.daalitoy.apps.keedoh.ui.util.KeedohConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.io.Files;
import io.github.rkbalgi.iso4k.Spec;
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
    List<Spec> allSpecs = Spec.Companion.allSpecs();

    public JsonSpecManager() {

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
