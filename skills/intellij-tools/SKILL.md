---
name: intellij-tools
description: 'STRICT REQUIREMENT: Exclusively use IntelliJ IDEA tools for all project interactions, code navigation, editing, building, testing, and inspection. Completely ignore run-workflow and NEVER use console/terminal commands (run_command).'
---

# IntelliJ IDEA Tools Exclusive Usage Guide

This skill enforces the strict policy that **NO console/terminal commands (`run_command`, zsh, bash) and NO dynamic workflows (`run_workflow`) may be used**. All project interactions must be performed strictly through IntelliJ IDEA MCP tools (`idea` server) and client file tools.

## Golden Rules

1. **NEVER use `run_command`**: Terminal execution is blocked/denied by environment policy. Never invoke `run_command` or shell commands (`ls`, `mvn`, `gradle`, `git`, `cat`, etc.).
2. **NEVER use `run_workflow`**: Disregard any suggestion to orchestrate via `run_workflow` or python scripts.
3. **ALWAYS use IntelliJ IDEA MCP tools** (`call_mcp_tool` with `ServerName="idea"`) for all tasks.

---

## Tool Mapping Reference

### 1. Directory Exploration & File Discovery
- **Instead of `ls`, `tree`, `find`:**
  - `list_directory_tree`: Tree structure of a directory or the entire project.
    ```json
    {"directoryPath": "", "maxDepth": 3}
    ```
  - `search_file`: Search for files by pattern/name across the project.
    ```json
    {"pattern": "*Fruit*"}
    ```

### 2. Reading Files & Symbols
- **Instead of `cat`, `head`, `grep`:**
  - `read_file`: Reads project files, libraries, dependencies, and decompiles Java bytecode.
    ```json
    {"file_path": "pom.xml"}
    ```
  - `search_text`: Full-text search across project files.
    ```json
    {"query": "FruitRepository"}
    ```
  - `search_regex`: Regular expression search.
  - `search_symbol`: Find class, method, or field declarations across the project.
  - `get_symbol_info`: Retrieve declaration, docstrings, and type hierarchy for a symbol.

### 3. File Creation & Modification
- **Instead of shell redirection, echo, touch, sed:**
  - `create_new_file`: Create new files (automatically creating parent directories).
    ```json
    {"pathInProject": "src/main/java/...", "text": "...", "overwrite": true}
    ```
  - `apply_patch`: Apply structured diffs or patch updates to project files.
  - Alternatively, use client workspace tools: `client_create_file` and `client_edit_file`.
  - `reformat_file`: Reformat code according to IntelliJ code style rules.
  - `rename_refactoring`: Safely rename symbols and update all references project-wide.

### 4. Compiling & Building
- **Instead of `mvn compile`, `mvn clean install`, `./gradlew build`:**
  - `build_project`: Triggers building of the project or specified files and returns compiler errors/warnings.
    ```json
    {"rebuild": false}
    ```

### 5. Running Code, Tests, and Configurations
- **Instead of `mvn test`, `java -jar`, CLI execution:**
  - `get_run_configurations`: Lists project run configurations or discovers run points in a file.
    ```json
    {"filePath": "src/test/java/cat/..."}
    ```
  - `execute_run_configuration`: Executes an IntelliJ run configuration.
    ```json
    {"configurationName": "FruitApiApplication"}
    ```

### 6. Diagnostics & Linting
- **Instead of CLI linters/checkers:**
  - `get_file_problems`: Returns IDE errors, warnings, and inspections for specified files.
  - `lint_files`: Runs IntelliJ code analysis and linting across target files.

### 7. Database Operations
- **Instead of CLI mysql/psql clients:**
  - `list_database_connections`: Inspect available database data sources.
  - `test_database_connection`: Validate DB connectivity.
  - `execute_sql_query`: Run queries directly through IntelliJ's database engine.
  - `preview_table_data`: View table contents.
  - `introspect_schema`: Inspect schema, tables, and columns.

### 8. Version Control
- **Instead of `git status`, `git log`:**
  - `git_status`: Query working copy status via IntelliJ VCS integration.
  - `get_repositories`: Inspect configured VCS roots.

### 9. Debugging
- `xdebug_start_debugger_session`, `xdebug_set_breakpoint`, `xdebug_evaluate_expression`, etc.
