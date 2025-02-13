# Holiday Checker SDK

## 📘 Overview
The **Holiday Checker SDK** is an Android SDK that allows client applications to check if a given date is a holiday using multiple APIs. It integrates **Hilt**, **Retrofit**, and **Room** to provide efficient network calls, caching, and dependency injection.

---

## 🔧 Installation & Setup

### Add Dependencies
Add the required dependencies to your `build.gradle.kts`:

```kotlin
implementation project(':HolidayCheckerSDK')
```

---

## 🛠️ Using the SDK

### 1. Inject `HolidayUseCase` in ViewModel
```kotlin
@HiltViewModel
class HolidayViewModel @Inject constructor(
    private val holidayUseCase: HolidayUseCase
) : ViewModel() {

    fun checkHoliday(year: Int, month: Int, day: Int) {
        viewModelScope.launch {
            ...
            holidayUseCase.execute(year, month, day)
            ...
        }
    }
}
```

### 2. Observe Results in Activity/Fragment
```kotlin
class MainActivity : AppCompatActivity() {
    private val viewModel: HolidayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.isHoliday.observe(this) { result ->
            when (result) {
                is Result.Loading -> showLoading()
                is Result.Success -> showHolidayStatus(result.data)
                is Result.Error -> showError(result.message)
            }
        }

        viewModel.checkHoliday(2024, 12, 25)
    }
}
```

---

## 🔍 Explanation of Design Decisions

### 🛠️ Clean Architecture
The SDK follows **Clean Architecture**, separating concerns into:
- **Presentation Layer** → `ViewModel`
- **Domain Layer** → `UseCase`
- **Data Layer** → `Repository`, `DataSources`, `Retrofit`, `Room`

### Data Flow Diagram
```
ViewModel → UseCase → Repository → [Cache Data Source (Room) OR Remote Data Source (Retrofit APIs)]
```
- **First, check the cache (Room)**
- **If no data, fetch from APIs and cache it**
- **Return result to UI**

### Error Handling
- Uses `Result<T>` wrapper:
```kotlin
sealed class Result<out T> {
    object Loading : Result<Nothing>()
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val message: String) : Result<Nothing>()
}
```
- Helps in **better debugging** and **provides useful error messages** to client apps.

### Performance Optimizations
✅ **Room Database** caching reduces redundant API calls  
✅ **Coroutine Dispatchers.IO** ensures non-blocking operations  
✅ **Retrofit Response Handling** prevents UI crashes

---

## 📌 Handling Different API Responses
Since each API has a **different response format**, we normalize the responses into a **common model**:
```kotlin
data class UnifiedHolidayResponse(val isHoliday: Boolean)
```
Each API response is **mapped** into this format:
```kotlin
override suspend fun fetchHolidays(year: Int, month: Int, day: Int): Boolean {
    return try {
        val api1 = calendarificService.getHolidays(API_KEY, "US", year, month, day)
        val api2 = abstractApiService.getHolidays(API_KEY, "US", year, month, day)
        val api3 = holidayApiService.getHolidays(API_KEY, "US", year, month, day)

        val holidays = listOf(api1, api2, api3).any { it.response.holidays.isNotEmpty() }
        holidays
    } catch (e: Exception) {
        false
    }
}
```
- **Ensures consistency** across different APIs
- **Handles failures gracefully** (returns `false` if all APIs fail)

---

## ✅ Debugging & Troubleshooting
### Common Errors
| Issue | Solution |
|---|---|
| API request failing | Check **API keys** and **network connectivity** |
| Database not storing holidays | Ensure **Room database is properly initialized** |
| UI not updating | Observe **LiveData** and handle **state changes properly** |

### Enable Logging
Use `HttpLoggingInterceptor` to log API responses:
```kotlin
val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
val client = OkHttpClient.Builder().addInterceptor(logging).build()

Retrofit.Builder()
    .client(client)
    .baseUrl(BASE_URL)
    .addConverterFactory(GsonConverterFactory.create())
    .build()
```

---

## 📌 Summary
✔ **Easy Integration** with ViewModel & LiveData  
✔ **Clean Architecture** for scalability  
✔ **Error Handling** for debugging failures  
✔ **Caching with Room** to improve performance

---