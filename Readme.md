# 🌿 Seravia — Bridging the Gap in Mental Health with AI

Seravia is an AI-powered mental wellness platform that helps users reflect, heal, and connect between therapy sessions.
With powerful journaling tools, empathetic AI chats, auto-generated reports, and therapist collaboration — Seravia is
designed to make therapy more continuous, safe, and stigma-free.

---

## 📌 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Getting Started](#-getting-started)
- [Contributing](#-contributing)
- [Roadmap](#-roadmap)
- [License](#-license)

---

## ✨ Features

### 📓 AI-Powered Journaling

- Distraction-free journaling space inspired by Apple Notes.
- Smart caching: journals are available offline after login.
- AI generates gentle insights and summaries based on user tone.

### 💬 Conversational AI Chat

- Talk to AI in different tones: friendly, professional, or playful.
- Designed for venting, light reflection, or emotional release.
- Chat memory handled via Cassandra for privacy-focused context.

### 🆘 Suicide Prevention & Crisis Detection

- Built-in tool calling flags potential crisis messages.
- **Two-step safety chain**:
    1. AI detects high-risk phrases (e.g., suicidal ideation).
    2. It is again prompted to the llm for safety issues for example being suicidal. If true then
    3. Assigned therapist is alerted immediately with minimal, context-aware details.
- No external authority contact without user consent.
- AI responds with gentle prompts encouraging journaling or therapist outreach.

### 📊 Auto-Generated Reports

- Reports are generated weekly, bi-weekly, or monthly (user preference).
- Emotional trends, journaling patterns, and self-reflection summaries.
- Therapists access reports for deeper, ongoing understanding.

### 🧍 Personalized Preferences

- UI theme (Light/Dark)
- Report frequency
- Therapist preferences (age, gender, approachability)
- These guide AI tone and personalization.

### 🧑‍⚕️ Therapist Dashboard

- Therapist registration with license upload.
- Define therapy types.
- View AI-generated reports from subscribed users.
- Secure, in-app messaging with clients.

### 🔍 Symptom-Based Therapist Matching

- Users answer guided, symptom-based questions.
- AI maps symptoms to potential therapy styles/disorders.
- Suggests relevant therapists — no clinical diagnosis.

### 🎮 Gamified Wellness

- Daily streaks and milestone rewards.
- Designed to reduce therapy stigma and encourage consistency.
- Especially tailored for young users and Indian cultural context.

### 🔐 Data Privacy & Security

- No sensitive data shared with AI processors.
- Passwords hashed; data stored in MongoDB.
- Users can permanently delete their account and all data.
- JWT-based authentication.

---

## 🛠 Tech Stack

### Backend

- Java 21 + Spring Boot(3.3.4)
- MongoDB (Journals, Users)
- Milvus for vector storage
- Open AI gpt-4 and text-3-embedding-small for embeddings
- Cassandra (Chat Memory for Heavy Writes)
- Spring Security with JWT (access-only)
- Virtual Threads for heavy IO
- GraalVM (planned)
- Cron Jobs for report scheduling

### Frontend

- React.js (Web UI)
- React Native (Expo) Mobile(Android/IOS)
- Tab-based UI: Journals | Chat | Reports
- Cached journal data for fast, offline access
- Dynamic theming (Dark/Light modes)

---

## 🚀 Getting Started

### Backend Setup

```bash
git clone https://github.com/your-org/seravia-backend.git
cd seravia-backend
./mvnw spring-boot:run
