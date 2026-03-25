package me.ilucah.audiovisualiser_service.controller;

import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.models.ChatModel;
import com.openai.models.ReasoningEffort;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import me.ilucah.audiovisualiser_service.model.PromptResolve;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;

@RestController
public class PromptController {

    @Value("${openai.api-key}")
    private String apiKey;

    private OpenAIClientAsync client;

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClientAsync.builder()
                .apiKey(apiKey)
                .build();
    }

    @PostMapping("/prompt")
    public String prompt(@RequestBody String prompt) {
        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model(ChatModel.GPT_5_4)
                .addSystemMessage("""
                                You generate OpenGL GLSL 330 core shaders.
                                Output ONLY the fragment shader code.
                                No explanations.
                                No markdown.
                                No extra text.
                                The uniforms you have access to are:
                                uniform int time;
                                uniform float leftRMS;
                                uniform float rightRMS;
                                uniform float screenWidth;
                                uniform float screenHeight;
                                uniform float audioBufferTD[256];
                                The uniform audioBufferTD is a buffer of data in the time domain.
                                """)
                .addUserMessage(prompt)
                .reasoningEffort(ReasoningEffort.XHIGH)
                .build();
        try {
            ChatCompletion completion = client.chat()
                    .completions()
                    .create(params)
                    .get();

            String response = completion.choices()
                    .get(0)
                    .message()
                    .content().get();

            return PromptResolve.builder()
                    .success(true)
                    .prompt(response)
                    .build()
                    .toJson();

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("OpenAI request failed", e);
        }
    }
}