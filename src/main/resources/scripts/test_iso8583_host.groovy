import com.daalitoy.apps.keedoh.messaging.MessageHandler
import com.daalitoy.apps.keedoh.messaging.SpecMsg

class Iso8583MessageHandler implements MessageHandler {

    void handleMsg(SpecMsg msg) {


        def reqMsgType = msg.getValue(REQUEST, "Message Type")

        if (reqMsgType == "1100") {
            msg.setValue(RESPONSE, "Message Type", "1110")

            //by default turn off some fields
            msg.set(RESPONSE, "Bitmap", 2, false)

            def amount = Integer.parseInt(msg.getValue(REQUEST, "Bitmap", 4))
            if (amount == 450) {
                msg.setValue(RESPONSE, "Bitmap", 38, "ABC001")
                msg.setValue(RESPONSE, "Bitmap", 39, "000")
            } else if (amount == 451) {
                msg.setValue(RESPONSE, "Bitmap", 39, "100")
            }
        }
    }
}


