package com.daalitoy.apps.keedoh.messaging;

import com.daalitoy.apps.keedoh.data.model.Field;
import com.daalitoy.apps.keedoh.data.transit.FieldData;
import com.daalitoy.apps.keedoh.data.transit.MessageData;

import java.util.List;

public class MessagingUtils {

    public static String buildFlightKey(MessageData msgData) {

        StringBuilder builder = new StringBuilder();
        builder.append(String.format("[%s][%s]", msgData.getMessage().getSpec()
                .getSpecName(), msgData.getMessage().getMsgName()));
        for (Field f : msgData.getMessage().getRequestSegment().getFields()) {
            process(f, msgData, builder);
        }
        return (builder.toString());

    }

    private static void process(Field field, MessageData msgData,
                                StringBuilder builder) {

        if (field.isFlightKey()) {
            builder.append(String.format("[%s]", msgData.getFieldData(field)
                    .getStringData()));
        }
        for (Field child : field.getChildren()) {
            process(child, msgData, builder);
        }
        // }

    }

    public static String toHtml(MessageData rqMsgData, MessageData rpMsgData) {
        StringBuilder builder = new StringBuilder();
        builder.append("<html><body>\n");
        if (rqMsgData != null) {
            builder.append(String
                    .format("<table border=\"1\" style=\"font-family:verdana;font-size:8px;color:blue\">\n<tr><td align=\"center\" colspan=\"2\">%s</td></tr>",
                            "Request"));
            List<Field> fields = rqMsgData.getMessage().getRequestSegment()
                    .getFields();
            for (Field f : fields) {
                export(f, rqMsgData, builder);
            }
            builder.append("</table>\n");
        }
        builder.append("</br></br>\n");
        if (rpMsgData != null) {
            builder.append(String
                    .format("<table border=\"1\" style=\"font-family:verdana;font-size:8px;color:blue\">\n<tr><td align=\"center\" colspan=\"2\">%s</td></tr>",
                            "Response"));
            List<Field> fields = rpMsgData.getMessage().getRequestSegment()
                    .getFields();
            for (Field f : fields) {
                export(f, rpMsgData, builder);
            }
            builder.append("</table>\n");
        }

        builder.append("</body></html>\n");
        return (builder.toString());

    }

    private static void export(Field f, MessageData msgData,
                               StringBuilder builder) {
        builder.append(String.format("<tr><td>%s</td><td>%s</td></tr>",
                f.getFieldName(), msgData.getFieldData(f).getStringData()));
        for (Field child : f.getChildren()) {
            export(child, msgData, builder);
        }

    }

    public static void merge(MessageData reqMsgData, MessageData respMsgData) {

        List<Field> fields = respMsgData.getResponseFields();
        for (Field f : fields) {
            mergeValues(f, reqMsgData, respMsgData);
        }

    }

    private static void mergeValues(Field f, MessageData reqMsgData,
                                    MessageData respMsgData) {
        FieldData fieldData = reqMsgData.getFieldData(f);
        if (fieldData != null) {
            respMsgData.getFieldData(f).setData(fieldData.getData());
            respMsgData.getFieldData(f).setSelected(fieldData.isSelected());
            for (Field child : f.getChildren()) {
                mergeValues(child, reqMsgData, respMsgData);
            }
        }

    }
}
