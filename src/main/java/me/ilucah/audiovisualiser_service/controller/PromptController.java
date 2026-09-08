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
                .model(ChatModel.GPT_6_ASTRA)
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
                                When using polar coordinates such as atan(y, x) to map data around a
                                circular shape, treat the angular coordinate as periodic.
                                
                                Never create a discontinuity at the atan() wrap boundary (-PI / +PI).
                                When sampling an array or waveform using an angular coordinate, use
                                periodic/circular indexing so the final sample wraps back to the first
                                sample.
                                
                                For example, use:
                                x = fract(x);
                                i = int(floor(x * N)) % N;
                                j = (i + 1) % N;
                                
                                Do not clamp a circular coordinate to [0, 1] and independently sample
                                element 0 and element N-1, as this creates a visible seam.
                                """)
                .addUserMessage(prompt)
                .reasoningEffort(ReasoningEffort.MEDIUM)
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