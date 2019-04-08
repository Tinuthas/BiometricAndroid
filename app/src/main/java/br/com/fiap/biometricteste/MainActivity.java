package br.com.fiap.biometricteste;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    private ExecutorService executorService;
    private FragmentActivity fragmentActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        executorService = Executors.newSingleThreadExecutor();
        fragmentActivity = this;

        final BiometricPrompt biometricPrompt = biometric();
        final BiometricPrompt.PromptInfo promptInfo = authetic();

        Button auth = findViewById(R.id.auth);
        auth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    biometricPrompt.authenticate(promptInfo);
                }catch (Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public BiometricPrompt.PromptInfo authetic() {
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometria")
                .setSubtitle("Coloque sua digital")
                .setDescription("")
                .setNegativeButtonText("Cancelar")
                .build();
        return promptInfo;

    }

    public BiometricPrompt biometric() {
        BiometricPrompt biometricPrompt = new BiometricPrompt(fragmentActivity, executorService,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationSucceeded(
                            @NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        runOnUiThread(new Runnable() {
                            public void run() {
                                final Toast toast = Toast.makeText(MainActivity.this,
                                        "Sucesso Biometria", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        runOnUiThread(new Runnable() {
                            public void run() {
                                final Toast toast = Toast.makeText(MainActivity.this,
                                        "Digital incorreta", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }

                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull final
                    CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);


                        if(errorCode == android.hardware.biometrics.BiometricPrompt
                                .BIOMETRIC_ERROR_NO_BIOMETRICS)
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    final Toast toast = Toast.makeText(MainActivity.this,
                                            "Não tem biometria cadastrada", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });

                        if(errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    final Toast toast = Toast.makeText(MainActivity.this,
                                            "Botão Negativo", Toast.LENGTH_SHORT);
                                    toast.show();
                                }
                            });
                        }
                        runOnUiThread(new Runnable() {
                            public void run() {
                                final Toast toast = Toast.makeText(MainActivity.this,
                                        errString, Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        });
                    }
                });
        return biometricPrompt;
    }
}
