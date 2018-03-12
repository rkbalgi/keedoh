package com.daalitoy.apps.keedoh.data.model;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

public class Spec extends Model {

    private int specId;
    private String specName;
    private List<Field> fields = Lists.newArrayList();
    private Map<Integer, Field> fieldsMap = Maps.newHashMap();
    private List<Message> messages;

    public Spec() {
    }

    public Spec(int specId, String specName) {
        super();
        this.specId = specId;
        this.specName = specName;
    }

    public int getSpecId() {
        return specId;
    }

    public void setSpecId(int specId) {
        this.specId = specId;
    }

    public String getSpecName() {
        return specName;
    }

    public void setSpecName(String specName) {
        this.specName = specName;
    }

    public String toString() {
        return (specName);
    }

    public Field getFieldById(int fieldId) {
        return (fieldsMap.get(fieldId));
    }


    public void add(Field field) {
        fields.add(field);
    }

    public List<Field> getFields() {
        return (fields);
    }

    public void remove(Field field) {
        fields.remove(field);
    }

    public List<Message> getMessages() {
        return (messages);
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public Field getFieldByName(String fieldName) {
        for (Field field : fields) {
            if (field.getFieldName().equals(fieldName)) {
                return (field);
            }
        }
        return (null);
    }

}
