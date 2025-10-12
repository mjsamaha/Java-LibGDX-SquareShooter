# Java LibGDX SquareShooter Git Workflow

This document outlines the professional Git workflow for the SquareShooter project, ensuring clean version control with two main branches: `main` and `dev`. It also includes GitHub issues, commits, merges, and pushing strategies.

---

## Branch Strategy

- **`main`**
    - Always stable and deployable.
    - No direct development; only merged features from `dev`.

- **`dev`**
    - Main development branch.
    - All new features, bug fixes, and milestones are committed here first.

- **Feature branches (optional)**
    - For large features or experimental changes.
    - Naming: `feature/<feature-name>` or `bugfix/<short-description>`

---

## Daily Workflow

### **1. Start of Work**
Pull the latest changes from both `main` and `dev`:

```bash
# Ensure you are on main
git checkout main
git pull origin main

# Switch to dev
git checkout dev
git pull origin dev
```

## Creating a Feature Branch (optional)

```Bash
git checkout -b feature/<feature-name> dev
```

Base the new branch off dev.
Work on your feature in this branch.


## Work on Dev
Make code changes.
Test locally to ensure things work.


## Commit Changes

Follow professional commit message conventions:

```Bash
git add .
git commit -m "<type>: <short-description>"
```

Commit types (examples):

feat: new feature
fix: bug fix
docs: documentation changes
style: formatting, indentation, no code change
refactor: code change without feature/bug impact
test: adding or fixing tests
chore: build process or maintenance


```Bash
git commit -m "feat: add basic player movement logic"
```


## Push Changes

```Bash
git push -u origin dev

# if using feature branch
git push -u origin feature/<feature-name>
```

## Creating GB Issues

Go to your repository on GitHub → Issues → New Issue.
Title: concise description of the task or bug.
Description: detailed explanation, steps to reproduce (if bug), or requirements (if feature).
Assign yourself or team members.
Use labels (e.g., bug, feature, enhancement, documentation) to categorize issues.
Reference issues in commits:

```Bash
git commit -m "fix: resolve player collision bug (#12)"
```

## Merging Dev into Main

Ensure dev is up to date
```Bash
git checkout dev
git pull origin dev
```

Switch to main and pull latest
```Bash
git checkout dev
git pull origin dev
```


Merge dev into main
```Bash
git merge dev
```

Resolve conflicts, if any, then commit
Push main to GitHub
```Bash
git push origin main
```

## Summary of Commands
```Bash
# Sync branches
git checkout main
git pull origin main
git checkout dev
git pull origin dev

# Work on a feature
git checkout -b feature/<feature-name> dev
# make changes
git add .
git commit -m "feat: short description (#issue-number)"
git push -u origin feature/<feature-name>

# Merge dev to main
git checkout main
git pull origin main
git merge dev
git push origin main
```


