# 🤝 Contributing to Frequency

Thank you for your interest in contributing to **Frequency**.

> [!IMPORTANT]
> This project follows strict engineering standards. Please review architecture and workflow guidance in this repository before contributing.

## 🛠️ The Workflow (Strict TDD)

We follow the **Red-Green-Refactor** cycle described in our [TDD Blueprint](../docs/tdd_quality_blueprint.md).

1.  **🔴 Red**: Write a failing test (JUnit 5 / MockK / Turbine) covering the requirement.
2.  **🟢 Green**: Implement the *minimal* code to pass the test.
3.  **🔵 Refactor**: Optimize architecture and clean up code.

**PRs without tests will be rejected immediately.**

## 📐 Coding Standards & Governance

Please refer to [Governance](../docs/governance.md) for detailed style guides.

* **Architecture**: Clean Architecture + MVVM (Unidirectional Data Flow).
* **Components**: Single File Components (SFC) for Jetpack Compose.
* **Style**: Google Android Style Guide (enforced via `ktlint` and `detekt`).
* **Language**: Kotlin 2.3 (No Java).
* **Warnings**: Treat warnings as errors.

## 🚀 Pull Request Process

1.  **Fork & Branch**: Use semantic branch names (e.g., `feature/lyrics-sync`, `fix/player-crash`).
2.  **Commit Messages**: Follow **Conventional Commits**:
    * `feat: ...` for new features
    * `fix: ...` for bug fixes
    * `refactor: ...` for code cleanup
    * `test: ...` for adding tests
3.  **Verification**: Run the following commands locally before pushing:
    ```bash
    ./gradlew lintDebug       # Must be free of errors
    ./gradlew testDebugUnitTest # Must pass
    ```
4.  **Coverage**: Ensure you meet the **80% coverage goal** for business logic.

## 🐞 Reporting Bugs

Please use the [Bug Report Template](ISSUE_TEMPLATE/bug_report.yml). Attach logs (`adb logcat`) and screenshots.

---

**Quick Links:**
* 🐞 [Report Bugs](../issues/new/choose)
* 💬 [Discussions](../discussions)
* 🚀 [Actions](../actions)
