# CryptoPulse

A production-quality cryptocurrency price tracker Android application built with Kotlin and Jetpack Compose.

## Features

- **Home Dashboard** — Market overview with popular coins, top gainers/losers, and personalized watchlist
- **Markets** — Full market list with sorting, pull-to-refresh, and shimmer loading states
- **Search** — Debounced cryptocurrency search powered by CoinGecko
- **Coin Detail** — Interactive price charts (1D/7D/30D/90D/1Y), market statistics, ATH/ATL data, and coin descriptions
- **Favorites** — Persistent watchlist stored locally with Room
- **Settings** — Currency selection (USD/EUR/GBP/INR), theme (System/Light/Dark), refresh interval
- **Offline Support** — Cache-first architecture with "Offline" indicator and cached data display
- **Error Handling** — User-friendly error states with retry for all network operations

## Architecture

```
UI (Compose) → ViewModel (StateFlow) → Repository → Remote (Retrofit) / Local (Room)
```

### Package Structure

| Package | Purpose |
|---|---|
| `data/remote/api` | Retrofit interface, OkHttp interceptor, API config |
| `data/remote/dto` | CoinGecko API response DTOs |
| `data/local/dao` | Room DAOs for cached data and favorites |
| `data/local/entity` | Room entities |
| `data/repository` | Repository implementations with cache-first strategy |
| `domain/model` | Clean domain models |
| `domain/repository` | Repository interfaces |
| `presentation/` | Screens, ViewModels, and reusable Compose components |
| `util/` | Number formatting, date utils, HTML sanitization |

## CoinGecko API Configuration

1. Get a free API key at [coingecko.com/en/api/pricing](https://www.coingecko.com/en/api/pricing)
2. Open `local.properties` in the project root
3. Set your key:

```properties
COINGECKO_API_KEY=your_actual_key_here
```

The key is read through `BuildConfig` at compile time and passed as a query parameter to the Demo API. It is never committed to version control.

> **Note**: The app also works without an API key, but will be subject to stricter rate limits.

## Building

### Prerequisites

- Android Studio Ladybug or newer
- JDK 17
- Android SDK 35

### Debug Build

```bash
./gradlew assembleDebug
```

### Release Build

```bash
./gradlew assembleRelease
```

## Running Tests

```bash
./gradlew test
```

## Caching Strategy

The app uses a **cache-first** approach:

1. On launch, immediately display cached Room data (if available)
2. Fetch fresh data from CoinGecko in the background
3. On success: update Room and refresh the UI
4. On failure: keep showing cached data with an "Offline" banner and timestamp

Favorites are stored in Room and persist across app restarts and network outages.

## Navigation

```
Bottom Nav: Home | Markets | Favorites | Settings
                ↓        ↓         ↓
         Coin Detail  Coin Detail  Coin Detail
                ↑
             Search
```

All navigation state is preserved when switching between bottom tabs.

## Tech Stack

| Technology | Purpose |
|---|---|
| Kotlin | Language |
| Jetpack Compose | UI Framework |
| Material 3 | Design system |
| Navigation Compose | Screen navigation |
| Retrofit + OkHttp | HTTP networking |
| Gson | JSON parsing |
| Room | Local database |
| DataStore | Preferences storage |
| Coil | Image loading |
| Coroutines + Flow | Asynchronous programming |
| StateFlow | Reactive UI state |

## Credits

Market data powered by [CoinGecko](https://www.coingecko.com/).
