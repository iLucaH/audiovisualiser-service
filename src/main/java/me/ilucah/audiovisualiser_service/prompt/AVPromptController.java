package me.ilucah.audiovisualiser_service.prompt;

import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClientAsync;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import me.ilucah.audiovisualiser_service.token.TokenHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.PostConstruct;

import java.util.concurrent.ExecutionException;

@RestController
public class AVPromptController {

    @Value("${openai.api-key}")
    private String apiKey;

    private OpenAIClientAsync client;

    @PostConstruct
    public void init() {
        this.client = OpenAIOkHttpClientAsync.builder()
                .apiKey(apiKey)
                .build();
    }

    @GetMapping("/prompt")
    public String prompt(@RequestParam Long token,
                         @RequestParam String prompt) {

        if (!TokenHandler.isValidToken(token)) {
            return PromptResolve.builder()
                    .success(false)
                    .prompt("Invalid token")
                    .build()
                    .toJson();
        }

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_5_2)
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