package com.aya.post4

import AppExecutor
import android.R
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.aya.post4.databinding.ActivityMainBinding
import com.aya.post4.DatabaseWarga
import com.aya.post4.Warga
import com.aya.post4.WargaDao


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: DatabaseWarga
    private lateinit var dao: WargaDao
    private lateinit var appExecutors: AppExecutor

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        appExecutors = AppExecutor()
        db = DatabaseWarga.getDatabase(applicationContext)
        dao = db.wargaDao()

        val statusAdapter = ArrayAdapter(
            this, R.layout.simple_spinner_item,
            listOf("Belum Menikah", "Menikah", "Cerai")
        )
        binding.spStatus.adapter = statusAdapter

        binding.btnSimpan.setOnClickListener {
            binding.btnSimpan.setOnClickListener {
                Toast.makeText(this, "1. Tombol Simpan Ditekan", Toast.LENGTH_SHORT).show()

                val nama = binding.etNama.text.toString()
                val nik = binding.etNik.text.toString()
                val kabupaten = binding.etKabupaten.text.toString()
                val kecamatan = binding.etKecamatan.text.toString()
                val desa = binding.etDesa.text.toString()
                val rt = binding.etRT.text.toString()
                val rw = binding.etRW.text.toString()
                val gender = if (binding.rbLaki.isChecked) "Laki-Laki" else "Perempuan"
                val status = binding.spStatus.selectedItem.toString()

                if (nik.length != 16) {
                    binding.etNik.error = "NIK harus 16 angka"
                    return@setOnClickListener
                }

                Toast.makeText(this, "2. NIK Lolos (16 digit)", Toast.LENGTH_SHORT).show()

                if (nama.isEmpty()) {
                    binding.etNama.error = "Nama tidak boleh kosong"
                    return@setOnClickListener
                }

                Toast.makeText(this, "3. Nama Lolos (Tidak Kosong)", Toast.LENGTH_SHORT).show()


                val warga = Warga(0, nama, nik, kabupaten, kecamatan, desa, rt, rw, gender, status)

                Toast.makeText(this, "4. Mencoba Simpan ke DB...", Toast.LENGTH_SHORT).show()

                appExecutors.diskIO.execute {
                    try {
                        dao.insert(warga)

                        appExecutors.mainThread.execute {
                            Toast.makeText(this, "5. BERHASIL SIMPAN!", Toast.LENGTH_SHORT).show()

                            binding.etNama.text.clear()
                            binding.etNik.text.clear()
                            binding.etKabupaten.text.clear()
                            binding.etKecamatan.text.clear()
                            binding.etDesa.text.clear()
                            binding.etRT.text.clear()
                            binding.etRW.text.clear()
                            binding.rgGender.clearCheck()
                            binding.spStatus.setSelection(0)
                            binding.etNik.error = null
                            binding.etNama.error = null
                        }
                    } catch (e: Exception) {
                        appExecutors.mainThread.execute {
                            Toast.makeText(this, "6. GAGAL: NIK SUDAH ADA!", Toast.LENGTH_LONG).show()
                            binding.etNik.error = "NIK ini sudah dipakai"
                        }
                    }
                }
            }

        }
        binding.btnSimpan.setOnClickListener {
            val nama = binding.etNama.text.toString().trim()
            val nik = binding.etNik.text.toString().trim()
            val kabupaten = binding.etKabupaten.text.toString().trim()
            val kecamatan = binding.etKecamatan.text.toString().trim()
            val desa = binding.etDesa.text.toString().trim()
            val rt = binding.etRT.text.toString().trim()
            val rw = binding.etRW.text.toString().trim()
            val gender = if (binding.rbLaki.isChecked) "Laki-Laki" else "Perempuan"
            val status = binding.spStatus.selectedItem.toString()

            if (nama.isEmpty() || nik.isEmpty() || kabupaten.isEmpty() || kecamatan.isEmpty() || desa.isEmpty() || rt.isEmpty() || rw.isEmpty()) {
                Toast.makeText(this, "Data harus terisi semua!", Toast.LENGTH_SHORT).show()

                if (nama.isEmpty()) binding.etNama.error = "Harus diisi"
                if (nik.isEmpty()) binding.etNik.error = "Harus diisi"
                if (kabupaten.isEmpty()) binding.etKabupaten.error = "Harus diisi"
                if (kecamatan.isEmpty()) binding.etKecamatan.error = "Harus diisi"
                if (desa.isEmpty()) binding.etDesa.error = "Harus diisi"
                if (rt.isEmpty()) binding.etRT.error = "Harus diisi"
                if (rw.isEmpty()) binding.etRW.error = "Harus diisi"

                return@setOnClickListener
            }

            if (nik.length != 16) {
                Toast.makeText(this, "NIK harus 16 angka!", Toast.LENGTH_SHORT).show()
                binding.etNik.error = "NIK harus 16 angka"
                return@setOnClickListener
            }

            val warga = Warga(0, nama, nik, kabupaten, kecamatan, desa, rt, rw, gender, status)

            appExecutors.diskIO.execute {
                try {
                    dao.insert(warga)

                    appExecutors.mainThread.execute {
                        Toast.makeText(this, "Data berhasil disimpan!", Toast.LENGTH_SHORT).show()

                        binding.etNama.text.clear()
                        binding.etNik.text.clear()
                        binding.etKabupaten.text.clear()
                        binding.etKecamatan.text.clear()
                        binding.etDesa.text.clear()
                        binding.etRT.text.clear()
                        binding.etRW.text.clear()
                        binding.rgGender.clearCheck()
                        binding.spStatus.setSelection(0)
                        binding.etNama.error = null
                        binding.etNik.error = null
                        binding.etKabupaten.error = null
                        binding.etKecamatan.error = null
                        binding.etDesa.error = null
                        binding.etRT.error = null
                        binding.etRW.error = null
                    }
                } catch (e: Exception) {
                    appExecutors.mainThread.execute {
                        Toast.makeText(this, "GAGAL: NIK '$nik' SUDAH ADA!", Toast.LENGTH_LONG).show()
                        binding.etNik.error = "NIK ini sudah dipakai"
                    }
                }
            }
        }
        binding.btnReset.setOnClickListener {
            binding.etNama.text.clear()
            binding.etNik.text.clear()
            binding.etKabupaten.text.clear()
            binding.etKecamatan.text.clear()
            binding.etDesa.text.clear()
            binding.etRT.text.clear()
            binding.etRW.text.clear()
            binding.rgGender.clearCheck()
            binding.spStatus.setSelection(0)

            appExecutors.diskIO.execute {
                dao.deleteAll()
                appExecutors.mainThread.execute {
                    Toast.makeText(this, "Semua data warga telah dihapus!", Toast.LENGTH_SHORT).show()
                }
            }
        }


        dao.getAllWarga().observe(this) { list ->
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1,
                list.map {
                    "${it.id}. ${it.nama} (${it.jenisKelamin}) - ${it.statusPernikahan}\nNIK: ${it.nik}\nAlamat: RT ${it.rt}/RW ${it.rw}, ${it.desa}, ${it.kecamatan}, ${it.kabupaten}"
                })
            binding.lvWarga.adapter = adapter
        }
    }
}