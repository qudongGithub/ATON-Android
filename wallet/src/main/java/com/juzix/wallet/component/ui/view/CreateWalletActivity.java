package com.juzix.wallet.component.ui.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.juzix.wallet.R;
import com.juzix.wallet.component.ui.base.MVPBaseActivity;
import com.juzix.wallet.component.ui.contract.CreateWalletContract;
import com.juzix.wallet.component.ui.presenter.CreateWalletPresenter;
import com.juzix.wallet.component.widget.ShadowButton;
import com.juzix.wallet.engine.WalletManager;
import com.juzix.wallet.utils.CheckStrength;
import com.juzix.wallet.utils.SoftHideKeyboardUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class CreateWalletActivity extends MVPBaseActivity<CreateWalletPresenter> implements CreateWalletContract.View, View.OnClickListener, TextWatcher, View.OnFocusChangeListener {

    Unbinder unbinder;
    @BindView(R.id.et_name)
    EditText mEtName;
    @BindView(R.id.tv_name_error)
    TextView mTvNameError;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.et_repeat_password)
    EditText mEtRepeatPassword;
    @BindView(R.id.iv_password_eyes)
    ImageView mIvPasswordEyes;
    @BindView(R.id.iv_repeat_password_eyes)
    ImageView mIvRepeatPasswordEyes;
    @BindView(R.id.tv_password_desc)
    TextView mTvPasswordDesc;
    @BindView(R.id.tv_password_error)
    TextView mTvPasswordError;
    @BindView(R.id.sbtn_create)
    ShadowButton mSbtnCreate;
    @BindView(R.id.tv_strength)
    TextView mTvStrength;
    @BindView(R.id.v_line1)
    View mVLine1;
    @BindView(R.id.v_line2)
    View mVLine2;
    @BindView(R.id.v_line3)
    View mVLine3;
    @BindView(R.id.v_line4)
    View mVLine4;
    @BindView(R.id.layout_password_strength)
    LinearLayout mPasswordStrengthLayout;

    private boolean mShowPassword;
    private boolean mShowRepeatPassword;

    public static void actionStart(Context context) {
        context.startActivity(new Intent(context, CreateWalletActivity.class));
    }

    @Override
    protected CreateWalletPresenter createPresenter() {
        return new CreateWalletPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        unbinder = ButterKnife.bind(this);
        initView();
        showPassword();
        showRepeatPassword();
    }

    @Override
    protected boolean immersiveBarViewEnabled() {
        return true;
    }

    private void initView() {

        mSbtnCreate.setOnClickListener(this);
        mEtName.addTextChangedListener(this);
        mEtPassword.addTextChangedListener(this);
        mEtPassword.setTypeface(Typeface.DEFAULT);
        mEtRepeatPassword.addTextChangedListener(this);
        mEtRepeatPassword.setTypeface(Typeface.DEFAULT);
        mIvPasswordEyes.setOnClickListener(this);
        mIvRepeatPasswordEyes.setOnClickListener(this);
        mEtName.setOnFocusChangeListener(this);
        mEtPassword.setOnFocusChangeListener(this);
        mEtRepeatPassword.setOnFocusChangeListener(this);
        showNameError("", false);
        showPasswordError("", false);
        SoftHideKeyboardUtils.assistActivity(this);
    }

    private void enableCreate(boolean enabled) {
        mSbtnCreate.setEnabled(enabled);
    }

    private void showPassword() {
        if (mShowPassword) {
            // 显示密码
            mEtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mEtPassword.setSelection(mEtPassword.getText().toString().length());
            mIvPasswordEyes.setImageResource(R.drawable.icon_open_eyes);
            mShowPassword = !mShowPassword;
        } else {
            // 隐藏密码
            mEtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEtPassword.setSelection(mEtPassword.getText().toString().length());
            mIvPasswordEyes.setImageResource(R.drawable.icon_close_eyes);
            mShowPassword = !mShowPassword;
        }
    }

    private void showRepeatPassword() {
        if (mShowRepeatPassword) {
            // 显示密码
            mEtRepeatPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            mEtRepeatPassword.setSelection(mEtRepeatPassword.getText().toString().length());
            mIvRepeatPasswordEyes.setImageResource(R.drawable.icon_open_eyes);
            mShowRepeatPassword = !mShowRepeatPassword;
        } else {
            // 隐藏密码
            mEtRepeatPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            mEtRepeatPassword.setSelection(mEtRepeatPassword.getText().toString().length());
            mIvRepeatPasswordEyes.setImageResource(R.drawable.icon_close_eyes);
            mShowRepeatPassword = !mShowRepeatPassword;
        }
    }

    @Override
    public void showNameError(String text, boolean isVisible) {
        mTvNameError.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mTvNameError.setText(text);
    }

    @Override
    public void showPasswordError(String text, boolean isVisible) {
        mTvPasswordError.setVisibility(isVisible ? View.VISIBLE : View.GONE);
        mTvPasswordError.setText(text);
        mTvPasswordDesc.setVisibility(isVisible ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sbtn_create:
                mPresenter.createWallet(mEtName.getText().toString().trim(),
                        mEtPassword.getText().toString().trim(),
                        mEtRepeatPassword.getText().toString().trim());
                break;
            case R.id.iv_password_eyes:
                showPassword();
                break;
            case R.id.iv_repeat_password_eyes:
                showRepeatPassword();
                break;
            default:
                break;
        }
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        String name = mEtName.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String repeatPassword = mEtRepeatPassword.getText().toString().trim();
        if (v == mEtName && !hasFocus) {
            if (TextUtils.isEmpty(name)) {
                showNameError(string(R.string.validWalletNameEmptyTips), true);
            } else if (name.length() > 12) {
                showNameError(string(R.string.validWalletNameTips), true);
            } else if (WalletManager.getInstance().isWalletNameExists(name)) {
                showNameError(string(R.string.wallet_name_exists), true);
            } else {
                showNameError("", false);
            }
        }
        if (v == mEtPassword && !hasFocus) {
            if (TextUtils.isEmpty(password)) {
                showPasswordError(string(R.string.validPasswordEmptyTips), true);
            } else if (password.length() < 6) {
                showPasswordError(string(R.string.validPasswordTips), true);
            } else {
                if (password.equals(repeatPassword)) {
                    showPasswordError("", false);
                }
            }
        }
        if (v == mEtRepeatPassword && !hasFocus) {
            if (TextUtils.isEmpty(repeatPassword)) {
                showPasswordError(string(R.string.validRepeatPasswordEmptyTips), true);
            } else if (!repeatPassword.equals(password)) {
                showPasswordError(string(R.string.passwordTips), true);
            } else {
                if (repeatPassword.equals(password) && password.length() >= 6) {
                    showPasswordError("", false);
                }
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        String name = mEtName.getText().toString().trim();
        String password = mEtPassword.getText().toString().trim();
        String repeatPassword = mEtRepeatPassword.getText().toString().trim();
        enableCreate(!TextUtils.isEmpty(name) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(repeatPassword) && password.length() >= 6 && name.length() <= 20);
        checkPwdStrength(password);
    }

    private void checkPwdStrength(String password) {

        mPasswordStrengthLayout.setVisibility(TextUtils.isEmpty(password) ? View.GONE : View.VISIBLE);

        if (TextUtils.isEmpty(password)) {
            mTvStrength.setText(R.string.strength);
            mVLine1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
            mVLine2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
            mVLine3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
            mVLine4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
            return;
        }
        switch (CheckStrength.getPasswordLevelNew(password)) {
            case EASY:
                mTvStrength.setTextColor(ContextCompat.getColor(getContext(), R.color.color_f5302c));
                mTvStrength.setText(R.string.weak);
                mVLine1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_f5302c));
                mVLine2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
                mVLine3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
                mVLine4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
                break;
            case MIDIUM:
                mTvStrength.setTextColor(ContextCompat.getColor(getContext(), R.color.color_ff9000));
                mTvStrength.setText(R.string.so_so);
                mVLine1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_ff9000));
                mVLine2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_ff9000));
                mVLine3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
                mVLine4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
                break;
            case STRONG:
                mTvStrength.setTextColor(ContextCompat.getColor(getContext(), R.color.color_58b8ff));
                mTvStrength.setText(R.string.good);
                mVLine1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_58b8ff));
                mVLine2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_58b8ff));
                mVLine3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_58b8ff));
                mVLine4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_00000000));
                break;
            case VERY_STRONG:
            case EXTREMELY_STRONG:
                mTvStrength.setTextColor(ContextCompat.getColor(getContext(), R.color.color_19a20e));
                mTvStrength.setText(R.string.strong);
                mVLine1.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_19a20e));
                mVLine2.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_19a20e));
                mVLine3.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_19a20e));
                mVLine4.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.color_19a20e));
                break;
            default:
                break;
        }
    }
}
