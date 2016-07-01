package p4.geretaxi.TarefasAssincronas;

import android.os.AsyncTask;

import p4.geretaxi.ClassesHelper.Mail;

/**
 * Created by belchior on 01/07/2016.
 */
public class TarefaMail {

    public AsyncTask<String, Void, Void> mailInfo = new AsyncTask<String, Void, Void>() {
        @Override
        protected Void doInBackground(String... params) {
            String email = params[0];
            String pass = params[1];
            mailRegisto(email, pass);
            return null;
        }
    };

    private boolean mailRegisto(String email, String pass) {
        boolean result = false;
        Mail mail = new Mail();
        String[] toArr = {mail.get_from()};
        mail.set_to(toArr);
        mail.set_subject("Confimação de registo na app GereTaxi");
        mail.set_body("Obrigado pelo registo a sua info é:\n Email: " + email + "\npassword: " +
                pass + "\nMensagem enviada pela GereTaxiApp");

        try {

            result = mail.send();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
