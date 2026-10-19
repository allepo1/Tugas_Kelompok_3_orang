package com.example.tugas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

/*
 * PALET BARU (teal) - semua pasangan teks/latar dipilih agar kontras >= 4.5:1
 *  - Ink (#10231F) di atas putih / PageBackground  -> sangat tinggi (> 14:1)
 *  - MutedInk (#42565A) di atas putih              -> sekitar 7:1
 *  - Putih di atas BrandTeal (#00695C)             -> sekitar 6.5:1
 *  - Aksen gelap di atas container pastel          -> > 5:1
 */
private val BrandTeal = Color(0xFF00695C)
private val BrandMint = Color(0xFFD3F1EA)
private val PageBackground = Color(0xFFF2F8F6)
private val Ink = Color(0xFF10231F)
private val MutedInk = Color(0xFF42565A)
private val WelcomeBg = Color(0xFF00574B)
private val WelcomeText = Color(0xFFE3F4F0)

private val SoftGreen = Color(0xFFDDF3DF)
private val SoftBlue = Color(0xFFDCE8FB)
private val SoftOrange = Color(0xFFFFE6C7)
private val SoftPink = Color(0xFFFBDDE7)
private val SoftIndigo = Color(0xFFE5E0FA)

private val AccentGreen = Color(0xFF1B5E20)
private val AccentBlue = Color(0xFF0D47A1)
private val AccentOrange = Color(0xFF8A4B00)
private val AccentPink = Color(0xFF8E244D)
private val AccentIndigo = Color(0xFF4A3A9C)

/** Ukuran minimum kartu pada grid adaptif (poin 3). Coba juga 220.dp untuk membandingkan. */
private val GridMinSize = 260.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KampusKuTheme {
                AdaptiveDashboard()
            }
        }
    }
}

@Composable
private fun KampusKuTheme(content: @Composable () -> Unit) {
    val colors = lightColorScheme(
        primary = BrandTeal,
        onPrimary = Color.White,
        primaryContainer = BrandMint,
        onPrimaryContainer = Ink,
        secondary = Color(0xFF4A635E),
        background = PageBackground,
        surface = Color.White,
        onSurface = Ink,
        onSurfaceVariant = MutedInk,
        tertiary = AccentGreen
    )
    MaterialTheme(colorScheme = colors, content = content)
}

/**
 * Satu halaman dashboard yang mengubah penempatan elemen berdasarkan
 * lebar ruang aplikasi yang tersedia, bukan semata-mata tipe perangkat.
 * Compact < 600 dp, Medium 600-839 dp, Expanded >= 840 dp.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AdaptiveDashboard() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "KampusKu",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Dashboard Perkuliahan",
                            style = MaterialTheme.typography.labelMedium,
                            color = MutedInk
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Filled.School,
                        contentDescription = "Logo KampusKu",
                        tint = BrandTeal,
                        modifier = Modifier.padding(horizontal = 16.dp).size(28.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Ink
                )
            )
        },
        containerColor = PageBackground
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            val currentWidth = maxWidth
            val widthClass = when {
                currentWidth < 600.dp -> "COMPACT · PONSEL"
                currentWidth < 840.dp -> "MEDIUM · LAYAR SEDANG"
                currentWidth < 1200.dp -> "EXPANDED · TABLET/LAPTOP"
                else -> "LARGE · LAYAR BESAR"
            }
            val showSideRail = currentWidth >= 840.dp

            Row(modifier = Modifier.fillMaxSize()) {
                if (showSideRail) {
                    DashboardSideRail()
                }
                DashboardContent(
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                    widthClassLabel = widthClass
                )
            }
        }
    }
}

/** Area navigasi hanya ilustrasi visual pada pertemuan layout. Navigasi aktif dibahas pada pertemuan berikutnya. */
@Composable
private fun DashboardSideRail() {
    Column(
        modifier = Modifier.width(88.dp).fillMaxHeight()
            .background(Color.White)
            .padding(top = 24.dp, start = 8.dp, end = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        SideRailItem(Icons.Filled.Dashboard, "Beranda", selected = true)
        SideRailItem(Icons.Filled.CalendarMonth, "Jadwal", selected = false)
        SideRailItem(Icons.Filled.MenuBook, "Materi", selected = false)
        SideRailItem(Icons.Filled.BarChart, "Nilai", selected = false)
    }
}

@Composable
private fun SideRailItem(icon: ImageVector, label: String, selected: Boolean) {
    val tileColor = if (selected) BrandMint else Color.Transparent
    val iconColor = if (selected) BrandTeal else MutedInk
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            color = tileColor,
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.size(width = 56.dp, height = 40.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = label, tint = iconColor)
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = iconColor)
    }
}

@Composable
private fun DashboardContent(modifier: Modifier, widthClassLabel: String) {
    val courses = listOf(
        CourseData("Pemrograman Mobile", "Jetpack Compose dan Kotlin", "Pertemuan 5 · Layout Adaptif", 0.72f, Icons.Filled.School, BrandTeal, BrandMint),
        CourseData("Basis Data", "Normalisasi dan perancangan ERD", "Tugas dikumpulkan Jumat", 0.56f, Icons.Filled.Science, AccentGreen, SoftGreen),
        CourseData("Struktur Data", "Array, list, dan pencarian", "Latihan mandiri", 0.84f, Icons.Filled.Code, AccentOrange, SoftOrange),
        CourseData("Analisis Data", "Statistik deskriptif", "Materi minggu ini", 0.41f, Icons.Filled.BarChart, AccentPink, SoftPink),
        // --- Kartu mata kuliah baru (poin 1) ---
        CourseData("Jaringan Komputer", "Model OSI, TCP/IP, dan konfigurasi subnetting jaringan lokal", "Praktikum Rabu · Lab Jaringan", 0.63f, Icons.Filled.Wifi, AccentBlue, SoftBlue),
        CourseData("Interaksi Manusia dan Komputer", "Prinsip desain antarmuka, usability testing, dan evaluasi heuristik", "Kumpulkan wireframe sebelum Senin", 0.37f, Icons.Filled.Palette, AccentIndigo, SoftIndigo),
        CourseData("Keamanan Informasi dan Manajemen Risiko Sistem", "Kriptografi dasar, autentikasi, serta analisis ancaman pada aplikasi", "Kuis singkat pada pertemuan berikutnya", 0.28f, Icons.Filled.Security, AccentGreen, SoftGreen)
    )
    val metrics = listOf(
        MetricData("Mata Kuliah", "${courses.size}", "Semester berjalan", Icons.Filled.MenuBook, BrandTeal, BrandMint),
        MetricData("Tugas Selesai", "12 / 15", "80% tugas terkumpul", Icons.Filled.AssignmentTurnedIn, AccentGreen, SoftGreen),
        MetricData("Jadwal Hari Ini", "3", "Kelas terjadwal", Icons.Filled.Schedule, AccentBlue, SoftBlue)
    )

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = GridMinSize),
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            WindowClassBanner(widthClassLabel)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            WelcomeCard()
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            SectionHeader("Ringkasan Akademik", "Kartu ringkasan akan berpindah sesuai lebar layar.")
        }
        items(metrics) { metric ->
            MetricCard(metric)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            SectionHeader("Mata Kuliah", "Grid menggunakan ukuran minimum ${GridMinSize.value.toInt()} dp agar kartu tetap nyaman dibaca.")
        }
        items(courses) { course ->
            CourseCard(course)
        }
        item(span = { GridItemSpan(maxLineSpan) }) {
            Text(
                text = "Data contoh untuk praktikum · Belum menggunakan database atau API",
                style = MaterialTheme.typography.bodySmall,
                color = MutedInk,
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
private fun WindowClassBanner(label: String) {
    Surface(
        color = BrandMint,
        contentColor = Ink,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 14.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = BrandTeal)
            Spacer(Modifier.width(8.dp))
            Text(
                "Mode layout: $label",
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun WelcomeCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = WelcomeBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(22.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Selamat datang, 107,097,083!", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(6.dp))
                Text("Atur kegiatan kuliahmu dengan lebih terarah. Tampilan ini menyesuaikan ruang layar yang tersedia.", color = WelcomeText, style = MaterialTheme.typography.bodyMedium)
            }
            Surface(shape = CircleShape, color = Color(0xFF2E7D73), modifier = Modifier.size(58.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.School, contentDescription = "Ilustrasi pendidikan", tint = Color.White, modifier = Modifier.size(32.dp))
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(top = 4.dp, bottom = 1.dp)) {
        Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Ink)
        Spacer(Modifier.height(3.dp))
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MutedInk)
    }
}

private data class MetricData(
    val label: String,
    val value: String,
    val detail: String,
    val icon: ImageVector,
    val accent: Color,
    val container: Color
)

@Composable
private fun MetricCard(data: MetricData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(color = data.container, shape = RoundedCornerShape(12.dp), modifier = Modifier.size(42.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(data.icon, contentDescription = null, tint = data.accent)
                    }
                }
                Spacer(Modifier.width(10.dp))
                // weight(1f) supaya label panjang membungkus ke baris baru, bukan terpotong
                Text(
                    data.label,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MutedInk,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(Modifier.height(12.dp))
            Text(data.value, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = Ink)
            Spacer(Modifier.height(2.dp))
            Text(data.detail, style = MaterialTheme.typography.bodySmall, color = MutedInk)
        }
    }
}

private data class CourseData(
    val title: String,
    val subtitle: String,
    val note: String,
    val progress: Float,
    val icon: ImageVector,
    val accent: Color,
    val container: Color
)

@Composable
private fun CourseCard(course: CourseData) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Surface(color = course.container, shape = RoundedCornerShape(14.dp), modifier = Modifier.size(48.dp)) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(course.icon, contentDescription = null, tint = course.accent, modifier = Modifier.size(26.dp))
                    }
                }
                // Poin 4: tanpa maxLines/ellipsis, teks panjang otomatis turun ke baris berikutnya
                Column(modifier = Modifier.weight(1f)) {
                    Text(course.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Ink)
                    Spacer(Modifier.height(4.dp))
                    Text(course.subtitle, style = MaterialTheme.typography.bodySmall, color = MutedInk)
                }
            }
            Spacer(Modifier.height(14.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text("Progres belajar", style = MaterialTheme.typography.labelMedium, color = MutedInk)
                Spacer(Modifier.weight(1f))
                Text("${(course.progress * 100).toInt()}%", style = MaterialTheme.typography.labelMedium, color = course.accent, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.height(6.dp))
            LinearProgressIndicator(
                progress = { course.progress },
                modifier = Modifier.fillMaxWidth().height(7.dp),
                color = course.accent,
                trackColor = course.container
            )
            Spacer(Modifier.height(10.dp))
            Text(course.note, style = MaterialTheme.typography.labelSmall, color = MutedInk)
        }
    }
}

// Poin 5: uji pada ponsel, layar sedang, dan tablet/layar lebar
@Preview(name = "Ponsel · Compact", showBackground = true, widthDp = 393, heightDp = 850)
@Composable
private fun PhonePreview() {
    KampusKuTheme { AdaptiveDashboard() }
}

@Preview(name = "Medium · 700dp", showBackground = true, widthDp = 700, heightDp = 900)
@Composable
private fun MediumPreview() {
    KampusKuTheme { AdaptiveDashboard() }
}

@Preview(name = "Tablet · Expanded", showBackground = true, widthDp = 1000, heightDp = 800)
@Composable
private fun TabletPreview() {
    KampusKuTheme { AdaptiveDashboard() }
}