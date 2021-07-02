package softuniBlog.bindingModel;

import com.sun.istack.NotNull;

public class ResetPasswordBindingModel {
    @NotNull
    private String newPassword;
    @NotNull
    private String confirmNewPassword;

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmNewPassword() {
        return confirmNewPassword;
    }

    public void setConfirmNewPassword(String confirmNewPassword) {
        this.confirmNewPassword = confirmNewPassword;
    }
}
