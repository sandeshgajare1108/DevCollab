
package com.DevCollab.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class GitHubService {

	@Autowired
	private RestTemplate restTemplate;

	private static final String GITHUB_API = "https://api.github.com";

	private static final String API_VERSION = "2026-03-10";

	// =====================================================
	// NORMAL JSON HEADERS
	// =====================================================

	private HttpEntity<String> createHeaders() {

		HttpHeaders headers = new HttpHeaders();

		headers.set("Accept", "application/vnd.github+json");

		headers.set("X-GitHub-Api-Version", API_VERSION);

		return new HttpEntity<String>(headers);
	}

	// =====================================================
	// GET REPOSITORY
	// =====================================================

	public Map<String, Object> getRepository(String owner, String repo) {

		validateOwnerRepo(owner, repo);

		String url = GITHUB_API + "/repos/" + owner + "/" + repo;

		ResponseEntity<Map> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, createHeaders(),
				Map.class);

		return response.getBody();
	}

	// =====================================================
	// GET COMMITS
	// =====================================================

	public List<Map<String, Object>> getCommits(String owner, String repo) {

		validateOwnerRepo(owner, repo);

		String url = GITHUB_API + "/repos/" + owner + "/" + repo + "/commits?per_page=20";

		ResponseEntity<Map[]> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, createHeaders(),
				Map[].class);

		Map[] body = response.getBody();

		if (body == null) {
			return new ArrayList<Map<String, Object>>();
		}

		return Arrays.asList(body);
	}

	// =====================================================
	// GET PULL REQUESTS
	// =====================================================

	public List<Map<String, Object>> getPullRequests(String owner, String repo, String state) {

		validateOwnerRepo(owner, repo);

		if (state == null || state.trim().isEmpty()) {

			state = "open";
		}

		String url = GITHUB_API + "/repos/" + owner + "/" + repo + "/pulls?state=" + state + "&per_page=100";

		ResponseEntity<Map[]> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, createHeaders(),
				Map[].class);

		Map[] body = response.getBody();

		if (body == null) {
			return new ArrayList<Map<String, Object>>();
		}

		return Arrays.asList(body);
	}

	// =====================================================
	// GET SINGLE PULL REQUEST
	// =====================================================

	public Map<String, Object> getPullRequest(String owner, String repo, Long pullRequestNumber) {

		validateOwnerRepo(owner, repo);

		validatePullRequestNumber(pullRequestNumber);

		String url = GITHUB_API + "/repos/" + owner + "/" + repo + "/pulls/" + pullRequestNumber;

		ResponseEntity<Map> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, createHeaders(),
				Map.class);

		return response.getBody();
	}

	// =====================================================
	// GET PULL REQUEST DIFF
	// =====================================================

	public String getPullRequestDiff(String owner, String repo, Long pullRequestNumber) {

		validateOwnerRepo(owner, repo);

		validatePullRequestNumber(pullRequestNumber);

		String url = GITHUB_API + "/repos/" + owner + "/" + repo + "/pulls/" + pullRequestNumber;

		HttpHeaders headers = new HttpHeaders();

		headers.set("Accept", "application/vnd.github.diff");

		headers.set("X-GitHub-Api-Version", API_VERSION);

		HttpEntity<String> entity = new HttpEntity<String>(headers);

		ResponseEntity<String> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, entity, String.class);

		String diff = response.getBody();

		if (diff == null || diff.trim().isEmpty()) {

			throw new RuntimeException("No changes found in this Pull Request");
		}

		return diff;
	}

	// =====================================================
	// GET PULL REQUEST FILES
	// =====================================================

	public List<Map<String, Object>> getPullRequestFiles(String owner, String repo, Long pullRequestNumber) {

		validateOwnerRepo(owner, repo);

		validatePullRequestNumber(pullRequestNumber);

		String url = GITHUB_API + "/repos/" + owner + "/" + repo + "/pulls/" + pullRequestNumber
				+ "/files?per_page=100";

		ResponseEntity<Map[]> response = restTemplate.exchange(URI.create(url), HttpMethod.GET, createHeaders(),
				Map[].class);

		Map[] body = response.getBody();

		if (body == null) {
			return new ArrayList<Map<String, Object>>();
		}

		return Arrays.asList(body);
	}

	// =====================================================
	// GET DEFAULT BRANCH
	// =====================================================

	public String getDefaultBranch(Map<String, Object> repository) {

		if (repository == null) {
			return null;
		}

		Object branch = repository.get("default_branch");

		return branch != null ? branch.toString() : null;
	}

	// =====================================================
	// PARSE REPOSITORY URL
	// =====================================================

	public String[] parseRepositoryUrl(String repositoryUrl) {

		if (repositoryUrl == null || repositoryUrl.trim().isEmpty()) {

			throw new RuntimeException("GitHub repository URL is required");
		}

		String url = repositoryUrl.trim();

		if (url.endsWith("/")) {

			url = url.substring(0, url.length() - 1);
		}

		if (url.endsWith(".git")) {

			url = url.substring(0, url.length() - 4);
		}

		String prefix = "https://github.com/";

		if (!url.startsWith(prefix)) {

			throw new RuntimeException(
					"Only GitHub URLs are supported. " + "Example: " + "https://github.com/owner/repository");
		}

		String path = url.substring(prefix.length());

		String[] parts = path.split("/");

		if (parts.length != 2) {

			throw new RuntimeException("Invalid GitHub repository URL");
		}

		String owner = parts[0].trim();

		String repo = parts[1].trim();

		if (owner.isEmpty() || repo.isEmpty()) {

			throw new RuntimeException("Invalid GitHub repository URL");
		}

		return new String[] { owner, repo };
	}

	// =====================================================
	// BUILD REPOSITORY URL
	// =====================================================

	public String buildRepositoryUrl(String owner, String repo) {

		return "https://github.com/" + owner + "/" + repo;
	}

	// =====================================================
	// VALIDATE OWNER / REPOSITORY
	// =====================================================

	private void validateOwnerRepo(String owner, String repo) {

		if (owner == null || owner.trim().isEmpty()) {

			throw new RuntimeException("GitHub owner is required");
		}

		if (repo == null || repo.trim().isEmpty()) {

			throw new RuntimeException("GitHub repository is required");
		}
	}

	// =====================================================
	// VALIDATE PR NUMBER
	// =====================================================

	private void validatePullRequestNumber(Long pullRequestNumber) {

		if (pullRequestNumber == null || pullRequestNumber <= 0) {

			throw new RuntimeException("GitHub Pull Request number is required");
		}
	}
}
