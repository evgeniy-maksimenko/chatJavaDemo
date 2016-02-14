import ui.ChatPanel;
import ui.LoginPanel;

public class Router {
    public Router(){
        LoginPanel loginPanel = new LoginPanel();
        loginPanel.showDialog();
        if (loginPanel.isResultOk) {
            String login = loginPanel.getLogin();
            ChatPanel chatPanel = new ChatPanel(login);
            chatPanel.showDialog();
        }
    }
}
