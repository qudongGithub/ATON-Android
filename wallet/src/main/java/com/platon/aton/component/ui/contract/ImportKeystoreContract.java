package com.platon.aton.component.ui.contract;

import com.platon.aton.component.ui.IContext;

public class ImportKeystoreContract {

    public interface View extends IContext {
        String getKeystoreFromIntent();
        void showQRCode(String QRCode);
        void showKeystoreError(String text, boolean isVisible);
        void showNameError(String text, boolean isVisible);
        void showPasswordError(String text, boolean isVisible);
        void enablePaste(boolean enabled);
    }

    public interface Presenter extends IPresenter<View> {
        void init();
        void parseQRCode(String QRCode);
        void importKeystore(String keystore, String name, String password);
        void checkPaste();
        boolean isExists(String walletName);
    }
}
