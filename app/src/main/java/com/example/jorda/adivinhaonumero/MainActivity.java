package com.example.jorda.adivinhaonumero;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends Activity {
    public final int MAIS = 1;
    public final int MENOS = 2;
    public final int ACERTOU = 3;
    public final int DIFICULDADE_1 = 2131165234;
    public final int DIFICULDADE_2 = 2131165235;
    public final int DIFICULDADE_3 = 2131165236;

    private TextView mNumeroTentativasTextView;
    private EditText mNumeroDigitadoEditText;
    private TextView mFeedbackTextView;
    private Button mTentarButton;
    private Button mResetarButton;
    private RadioGroup mDificuldadeRadioGroup;
    private RadioButton mDificuldade1RadioButton;

    private int mDificuldade;
    private int mNumeroGerado;
    private int mNumeroTentativas;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        geraNumero();

        mTentarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                comparaNumeroDigitadoComNumeroGerado();
            }
        });

        mResetarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetaJogo();
            }
        });

        mDificuldadeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                mDificuldade = i;
                resetaJogo();
            }
        });

        mNumeroDigitadoEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.KEYCODE_ENTER:
                        comparaNumeroDigitadoComNumeroGerado();
                }
                return false;
            }
        });
    }

    private void comparaNumeroDigitadoComNumeroGerado() {

        if (mNumeroDigitadoEditText.getText().toString().equals("")) {
            Toast.makeText(MainActivity.this, "Necessário digitar algum valor!", Toast.LENGTH_LONG).show();
            return;
        }

        atualizaNumeroDeTentativas();

        if (mNumeroGerado == Integer.parseInt(mNumeroDigitadoEditText.getText().toString())) {
            trocaMensagem(ACERTOU);

        } else if (mNumeroGerado > Integer.parseInt(mNumeroDigitadoEditText.getText().toString())) {
            trocaMensagem(MAIS);

        } else if (mNumeroGerado < Integer.parseInt(mNumeroDigitadoEditText.getText().toString())) {
            trocaMensagem(MENOS);
        }

        mNumeroDigitadoEditText.setText("");
    }

    private void atualizaNumeroDeTentativas() {
        mNumeroTentativas++;
        mNumeroTentativasTextView.setText(Integer.toString(mNumeroTentativas));
    }

    private void geraNumero() {
        Random random = new Random();

        switch (mDificuldade) {
            case DIFICULDADE_1:
                mNumeroGerado = random.nextInt(100);
                break;

            case DIFICULDADE_2:
                verificaSeDificuldade1Marcada();

                mNumeroGerado = random.nextInt(500);
                break;

            case DIFICULDADE_3:
                verificaSeDificuldade1Marcada();
                mNumeroGerado = random.nextInt(1000);
                break;

            default:
                mNumeroGerado = random.nextInt(100);
        }
    }

    private void verificaSeDificuldade1Marcada() {
        if (mDificuldade1RadioButton.isChecked()) {
            mDificuldade1RadioButton.toggle();
        }
    }

    private void resetaJogo() {
        mFeedbackTextView.setVisibility(View.GONE);
        mNumeroTentativasTextView.setText(R.string.default_tentativas_textview);
        mNumeroDigitadoEditText.setText("");
        mNumeroTentativas = 0;
        mTentarButton.setEnabled(true);
        geraNumero();
    }

    private void trocaMensagem(int estado) {
        mFeedbackTextView.setVisibility(View.VISIBLE);

        switch (estado) {
            case 1:
                mFeedbackTextView.setText(R.string.feedback_maior_textview);
                break;

            case 2:
                mFeedbackTextView.setText(R.string.feedback_menor_textview);
                break;

            case 3:
                jogadorGanhou();
                break;
        }
    }

    private void jogadorGanhou() {
        new AlertDialog.Builder(MainActivity.this)
                .setMessage(getString(R.string.feedback_acertou)
                        .replace("${value}", Integer.toString(mNumeroTentativas)))
                .show();
        mFeedbackTextView.setVisibility(View.GONE);
        mTentarButton.setEnabled(false);
        escondeTeclado();
    }

    private void initViews() {
        mNumeroTentativasTextView = findViewById(R.id.numero_tentativas_textview);
        mFeedbackTextView = findViewById(R.id.feedback_textview);
        mNumeroDigitadoEditText = findViewById(R.id.numero_edittext);
        mTentarButton = findViewById(R.id.tentar_button);
        mResetarButton = findViewById(R.id.resetar_button);
        mDificuldadeRadioGroup = findViewById(R.id.dificuldade_radiogroup);
        mDificuldade1RadioButton = findViewById(R.id.dificuldade_1_radiobutton);
    }

    public void escondeTeclado() {
        InputMethodManager inputMethodManager = (InputMethodManager) MainActivity.this.getSystemService(INPUT_METHOD_SERVICE);
        View view = MainActivity.this.getCurrentFocus();
        if (view == null) {
            view = new View(MainActivity.this);
        }
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}