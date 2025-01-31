package com.example.home;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.home.databinding.FragmentFirstBinding;
import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.java.GenerativeModelFutures;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;


    String key = "AIzaSyC6TNo0LqCSmtmviQb-XVtkpFxHJREemvk";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Infla il layout del fragment utilizzando il binding
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    // Chiamato subito dopo che il layout del fragment è stato inflato
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        GenerativeModel gm = new GenerativeModel("gemini-pro", key);
        GenerativeModelFutures model = GenerativeModelFutures.from(gm);
        Executor executor = Executors.newSingleThreadExecutor();

        binding.inviagemini.setOnClickListener(v -> {

            String prompt=String.valueOf(binding.geminiprompt.getText());
            Content content = new Content.Builder()
                    .addText(prompt)
                    .build();

            ListenableFuture<GenerateContentResponse> response = model.generateContent(content);
            Handler handler = new Handler(Looper.getMainLooper());
            Futures.addCallback(response, new FutureCallback<GenerateContentResponse>() {
                @Override
                public void onSuccess(GenerateContentResponse result) {
                    String resultText = result.getText();
                    handler.post(() -> {
                        binding.geminiresponse.setText(resultText);

                    });
                }

                @Override
                public void onFailure(Throwable t) {
                    handler.post(()->{
                        binding.geminiresponse.setText("fail");
                        t.printStackTrace();
                    });

                }
            }, executor);
            binding.geminiprompt.setText("");
        });





        // Imposta un listener per il click del pulsante
        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Naviga al secondo fragment quando il pulsante viene premuto
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });
    }

    // Chiamato quando la vista del fragment viene distrutta
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Imposta il binding su null per evitare memory leaks
        binding = null;
    }

}