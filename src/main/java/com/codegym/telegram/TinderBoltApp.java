package com.codegym.telegram;

import com.plexpt.chatgpt.ChatGPT;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.ArrayList;

public class TinderBoltApp extends SimpleTelegramBot {

    public static final String TELEGRAM_BOT_TOKEN = "7850379062:AAFi1-XtccfwTA0cPW9mzFhs_Bk5b-SJTys"; //TODO: añadir el token del bot entre comillas
    public static final String OPEN_AI_TOKEN = "gpt:yeSMIRCC7ePMWDmNqAy9JFkblB3T8MQ6g521gef0dCT5qsls"; //TODO: añadir el token de ChatGPT entre comillas

    private ChatGPTService chatGPT = new ChatGPTService(OPEN_AI_TOKEN);
    private DialogMode mode;
    private ArrayList<String> list = new ArrayList<>();

    public TinderBoltApp() {
        super(TELEGRAM_BOT_TOKEN);
    }

    //TODO: escribiremos la funcionalidad principal del bot aquí

    public void startcommand(){
        mode = DialogMode.MAIN;
        String text = loadMessage("main");
        sendPhotoMessage("main");
        sendTextMessage(text);

        showMainMenu("empezar", "Menú principal del bot",
                "perfil", "generación de perfil de Tinder \uD83D\uDE0E",
                "opener", "mensaje para iniciar conversación \uD83E\uDD70",
                "mensaje", "correspondencia en su nombre \uD83D\uDE08",
                "cita", "correspondencia con celebridades \uD83D\uDD25",
                "gpt", "hacer una pregunta a chat GPT \uD83E\uDDE0"
        );
    }

    public void gptCommand (){
        mode = DialogMode.GPT;

        String text = loadMessage("gpt");
        sendPhotoMessage("gpt");
        sendTextMessage(text);

    }

    public void gptDialog (){
        String text = getMessageText();
        String promp = loadPrompt("gpt");


        var mimensaje = sendTextMessage("chat gpt esta pensando \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0");
        String answer = chatGPT.sendMessage(promp, text);
        updateTextMessage(mimensaje,answer);
    }

    public void dateCommand (){
        mode = DialogMode.DATE;

        String text = loadMessage("date");
        sendPhotoMessage("date");
        sendTextMessage(text);
        sendTextButtonsMessage(text,
                "date_grande", "Ariana Grande",
                "date_robbie", "Margot Robbie",
                "date_zendaya", "Zendaya",
                "date_gosling", "Ryan Gosling",
                "date_hardy", "Tom Hardy");

    }

    public void dateButton(){
        String key = getButtonKey();
        sendPhotoMessage(key);
        sendHtmlMessage(key);

        String promp = loadPrompt(key);
        chatGPT.setPrompt(promp);
    }

    public void dateDialog(){
        String text = getMessageText();
        var mimensaje = sendTextMessage("el usuario esta escribiendo .....");
        String answer = chatGPT.addMessage(text);
//        sendTextMessage(answer);
        updateTextMessage(mimensaje, answer);
    }

    public void messagecommand(){
        mode = DialogMode.MESSAGE;
        String text = loadMessage("message");
        sendPhotoMessage("message");
        sendTextButtonsMessage(text,
                "message_next", "escribe el siguiente menssaje",
                "message_date", "preguntar si quiere una cita");
        list.clear();

    }

    public void messagebutton(){
        String key = getButtonKey();
        String promp = loadPrompt(key);
        String history = String.join("\n\n", list);

        var mimensaje = sendTextMessage("chat gpt esta pensando \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0");
        String answer = chatGPT.sendMessage(promp, history);
        updateTextMessage(mimensaje, answer);

    }

    public void messagedialog(){
        String text = getMessageText();
        list.add(text);

    }

    public void profilecommand(){
        mode = DialogMode.PROFILE;
        String text = loadMessage("profile");
        sendPhotoMessage("profile");
        sendTextMessage(text);

        sendTextMessage("cual es tu nombre");
        user = new UserInfo();
        questioncount = 0;


    }

    private UserInfo user = new UserInfo();
    private int questioncount = 0;

    public void profiledialog(){
        String text = getMessageText();
        questioncount++;

        if (questioncount == 1) {
            user.name = text;
            sendTextMessage("cual es tu edad");
        } else if (questioncount == 2) {
            user.age = text;
            sendTextMessage("que te gusta hacer en tus tiempos libres");
        }else if (questioncount == 3) {
            user.hobby = text;
            sendTextMessage("cual es tu objetivo para interactuar");
        } else if (questioncount == 4){
        user.goals = text;

            String promp = loadPrompt("profile");
            String userinfo = user.toString();

            var mymessage = sendTextMessage("chat gpt esta pensando \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0");
            String answer = chatGPT.sendMessage(promp, userinfo);
            updateTextMessage(mymessage, answer);

        }



    }

    public void openercommand(){
        mode = DialogMode.OPENER;
        String text = loadMessage("opener");
        sendPhotoMessage("opener");
        sendTextMessage(text);

        sendTextMessage("ingresa su nombre");
        user = new UserInfo();
        questioncount = 0;

    }

    public void openerdialog(){
        String text = getMessageText();
        questioncount++;

        if (questioncount == 1) {
            user.name = text;
            sendTextMessage("cual es su edad");
        } else if (questioncount == 2) {
            user.age = text;
            sendTextMessage("en que trabaja");
        }else if (questioncount == 3) {
            user.occupation = text;
            sendTextMessage("en la escala del 1 al 10 que tan atractiva es la persona");
        } else if (questioncount == 4) {
            user.handsome = text;

            String promp = loadPrompt("opener");
            String userinfo = user.toString();

            var mymessage = sendTextMessage("chat gpt esta pensando \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0 \uD83E\uDDE0");
            String answer = chatGPT.sendMessage(promp, userinfo);
            updateTextMessage(mymessage, answer);

        }

    }



    public void hello(){
        if (mode == DialogMode.GPT) {
            gptDialog();
        }else if (mode == DialogMode.DATE) {
            dateDialog();
        }else if (mode == DialogMode.MESSAGE) {
                messagedialog();
        }else if (mode == DialogMode.PROFILE) {
            profiledialog();
        } else if (mode == DialogMode.OPENER) {
            openerdialog();
        }else {
            String texto = getMessageText();
            sendTextMessage("*Hola*");
            sendTextMessage("_¿Como Estas?_");
            sendTextMessage("Escribiste: " + texto);

            sendPhotoMessage("date");
            sendTextButtonsMessage("iniciar proceso",
                    "inicio", "iniciar",
                    "parar", "detener");
        }
    }


    public void holaboton(){

        String key = getButtonKey();
        if (key.equals("inicio")) {
            sendTextMessage("*Hola gracias por iniciar el bot bienvenido a esta nueva y genial experiencia*");
        }else{
                sendTextMessage("_gracias por usar la experiencia vuelva pronto_");

        }

    }

    @Override
    public void onInitialize() {
        //TODO: y un poco más aquí :)
        addCommandHandler("empezar", this::startcommand);
        addCommandHandler("gpt", this::gptCommand);
        addCommandHandler("cita", this::dateCommand);
        addCommandHandler("mensaje", this::messagecommand);
        addCommandHandler("perfil", this::profilecommand);
        addCommandHandler("opener", this::openercommand);
        addMessageHandler(this::hello);
//        addButtonHandler("^.*", this::holaboton);
        addButtonHandler("^date_.*", this::dateButton);
        addButtonHandler("^message_.*", this::messagebutton);

    }

    public static void main(String[] args) throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        telegramBotsApi.registerBot(new TinderBoltApp());
    }
}
