package org.example;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import org.json.JSONArray;
import org.json.JSONObject;

public class ApiTest {
    private static final String URL = "https://jsonplaceholder.typicode.com/posts";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(URL))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        JSONArray posts = new JSONArray(response.body());

        // Scenario 1: Counting posts for a user
        int[][] testCasesUserPosts = {
                {5, 10},
                {7, 10},
                {9, 10}
        };

        for (int[] testCase : testCasesUserPosts) {
            countPostsForUser(posts, testCase[0], testCase[1]);
        }

        // Scenario 2: Unique ID per post
        checkUniqueIdPerPost(posts);
    }

    private static void countPostsForUser(JSONArray posts, int userId, int expectedCount) {
        int actualCount = 0;
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            if (post.getInt("userId") == userId) {
                actualCount++;
            }
        }
        assert actualCount == expectedCount : "User " + userId + " should have " + expectedCount + " posts, but found " + actualCount;
        System.out.println("Test passed: User " + userId + " has " + expectedCount + " posts.");
    }

    private static void checkUniqueIdPerPost(JSONArray posts) {
        HashSet<Integer> postIds = new HashSet<>();
        for (int i = 0; i < posts.length(); i++) {
            JSONObject post = posts.getJSONObject(i);
            int postId = post.getInt("id");
            if (!postIds.add(postId)) {
                throw new AssertionError("Duplicate post ID found: " + postId);
            }
        }
        System.out.println("Test passed: Each post has a unique ID.");
    }
}
