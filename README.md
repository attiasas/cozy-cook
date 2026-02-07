# Cozy Cook

Android app (Java) to manage your kitchen, pantry, recipes, meal plans, and grocery lists.

## Features

- **Kitchen items** – Define food, appliances, and other items for use across the app.
- **Pantry** – Track what you have (quantity, unit, optional expiry) for checklists and planning.
- **Recipes** – Add your own recipes (title, servings, time, ingredients, instructions) and search/filter/view them.
- **Recipe sources** – Fetch recipes from [Spoonacular](https://spoonacular.com/food-api/docs). The app is structured so you can add more sources (different APIs/schemas) later.
- **Meal plans** – Generate daily or weekly (or 2-week/month) plans from your recipes (e.g. random strategy; easy to extend).
- **Grocery list** – Generate a checklist from plans/recipes; check off as you buy.
- **Calendar** – Add “prep” or “grocery” events to the device calendar (intent-based; you can extend to Google Calendar API).

## Setup

1. Open the project in Android Studio and sync Gradle.
2. **Spoonacular API key** (optional): Get a free key at [spoonacular.com](https://spoonacular.com/food-api). In the app, open **Settings** (from the Recipes or main screen) and enter the key so “Search Spoonacular” works.

## Project structure

- `data/` – Room entities, DAOs, repositories, DB.
- `api/` – `RecipeSource` interface, `RecipeSourceResult`, Spoonacular client; add new sources by implementing `RecipeSource`.
- `ui/` – MainActivity (bottom nav), fragments (Pantry, Recipes, Plans, Grocery, Kitchen items), activities (Add Recipe, Recipe detail, Spoonacular search, Settings).
- `calendar/` – `CalendarHelper` for adding events (prep/grocery); replace or extend for more options.
- `util/` – `PlanGenerator`, `GroceryListGenerator`, `MealPlanRepositoryHelper`.

## Gradle wrapper

The project uses the **Gradle wrapper** (Gradle 8.14). You don’t need to install Gradle; run these from the project root.

### Common commands

| Command | Description |
|--------|-------------|
| `./gradlew --version` | Show Gradle and JVM version |
| `./gradlew tasks` | List available tasks |
| `./gradlew assembleDebug` | Build debug APK |
| `./gradlew assembleRelease` | Build release APK |
| `./gradlew installDebug` | Build and install debug on connected device/emulator |
| `./gradlew clean` | Remove build outputs |
| `./gradlew build` | Assemble and run tests |

On Windows use `gradlew.bat` instead of `./gradlew`.

### Regenerating the wrapper

To change the Gradle version or regenerate wrapper files:

```bash
./gradlew wrapper --gradle-version=8.14
```

## Building

```bash
./gradlew assembleDebug
```

Install on a device or emulator (API 24+).

## License

Use as you like; Spoonacular usage under their terms.
