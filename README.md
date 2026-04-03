# BudgetQuick (Rápido Presupuesto) 🏗️📱

![Platform](https://img.shields.io/badge/platform-Android-green)
![Language](https://img.shields.io/badge/language-Java-blue)
![Database](https://img.shields.io/badge/database-SQLite-orange)
![Methodology](https://img.shields.io/badge/methodology-RUP-red)

BudgetQuick is a native Android application designed to automate labor and material cost estimation directly on construction sites. It replaces manual note-taking and post-calculation in Excel, allowing contractors to deliver precise quotes in real-time.

Used in **20+ real projects**, it evolved from a production tool into a Software Engineering case study under the **Rational Unified Process (RUP)**.

---

## 📱 APK Download

You can download and test the app directly:

- 🔹 [Download BudgetQuick v2.0 APK](https://github.com/nokyslash/BudgetQuick/releases/tag/v2.0)

> Note: This APK is intended for demonstration purposes.

## 🧠 Engineering & Design
* **Methodology:** Developed using RUP (Rational Unified Process).
* **Detailed Modeling:** Includes Sequence diagrams (Export Path), Collaboration diagrams (Android Permissions/Security), and a robust Entity-Relationship Model (MER).
* **Data Integrity:** Implements a project-locking mechanism to preserve historical estimates.

## 🚀 Key Features
* **Smart Measurement System:** Dynamic UI for units, $m^2$, and $m^3$ with dimension multipliers.
* **Relational Domain:** Complex N:N relationships between Projects, Activities, and Materials.
* **On-Site UX:** High-contrast UI inspired by real-world construction tool colors for better visibility outdoors.

## 📂 Resources
* **Demo Videos:** 3 short MP4 clips showcasing the application's core functionality, located in `docs/demo-videos/`.
* **Technical Report:** Full engineering documentation available in `docs/report/informe-practica-profesional.pdf`.
* **Diagrams:** Use Cases, Sequence, and ER models available in `docs/diagrams/`.

---

## 🛠️ Technical Retrospect (Modernization)
As a legacy project, I recognize several areas for modernization:
1.  **Language:** Transition from Java to **Kotlin** for safety and Coroutines.
2.  **Architecture:** Migrate to **MVVM + Clean Architecture** (currently standard Android).
3.  **Persistence:** Upgrade raw SQLite to **Room** for compile-time safety.
4.  **UI:** Move from XML to **Jetpack Compose**.
5.  **Security:** Replace IMEI-based hardware locking with modern **Firebase Auth** or OAuth2.

---

## ⚠️ Status
This is a **Legacy Portfolio Project**. The application was originally built using older Android SDK versions.

Some legacy constraints (such as IMEI-based device restrictions) were **removed in the final version** to allow demonstration and evaluation.

The project is preserved primarily for **architectural and code-review purposes**, rather than production deployment.
