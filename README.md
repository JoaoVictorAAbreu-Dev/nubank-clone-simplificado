<h1>Nubank Clone Simplificado</h1>

<p>
  <img src="https://github.com/JoaoVictorAAbreu-Dev/nubank-clone-simplificado/actions/workflows/ci.yml/badge.svg" alt="CI" />
  <img src="https://img.shields.io/badge/Java-21-red" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.3.x-brightgreen" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/React-TypeScript-61DAFB" alt="React TypeScript" />
  <img src="https://img.shields.io/badge/Redis-7-red" alt="Redis" />
</p>

<p>
  Fullstack banking demo with JWT authentication, account balance control, fictional PIX, transfers, statement history, PostgreSQL persistence and Redis-backed read optimization.
</p>

<h2>Features</h2>
<ul>
  <li>User registration and login.</li>
  <li>Protected banking operations with JWT.</li>
  <li>Deposit, transfer and fictional PIX.</li>
  <li>Statement history.</li>
  <li>Redis cache for account balance reads.</li>
  <li>React interface with login, account summary and movement forms.</li>
</ul>

<h2>Architecture</h2>
<ul>
  <li><code>backend</code>: Spring Boot API with layered services.</li>
  <li><code>frontend</code>: React, TypeScript and Vite.</li>
  <li><code>docker-compose.yml</code>: PostgreSQL and Redis infrastructure.</li>
</ul>

<h2>Run Locally</h2>
<pre><code>docker compose up -d
.\mvnw.cmd -f backend\pom.xml spring-boot:run
cd frontend
npm install
npm run dev</code></pre>

<h2>Tests</h2>
<pre><code>.\mvnw.cmd -f backend\pom.xml test
cd frontend
npm run build</code></pre>

<h2>QA Checklist</h2>
<ul>
  <li>Backend tests included.</li>
  <li>Frontend production build verified.</li>
  <li>GitHub Actions CI configured.</li>
  <li>Swagger available at <code>http://localhost:8080/swagger-ui/index.html</code>.</li>
  <li>Docker Compose configured for PostgreSQL and Redis.</li>
  <li>Execution screenshot included.</li>
</ul>

<h2>Preview</h2>
<p>
  <img src="assets/home.png" alt="Nubank clone UI preview" style="max-width: 100%;" />
</p>
