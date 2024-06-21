package com.vnptit.ic.sample;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.vnptit.ic.sanmple.R;
import com.vnptit.ic.sanmple.databinding.ActivitySampleBinding;
import com.vnptit.idg.sdk.activity.VnptIdentityActivity;
import com.vnptit.idg.sdk.activity.VnptOcrActivity;
import com.vnptit.idg.sdk.activity.VnptPortraitActivity;
import com.vnptit.idg.sdk.utils.KeyIntentConstants;
import com.vnptit.idg.sdk.utils.KeyResultConstants;
import com.vnptit.idg.sdk.utils.SDKEnum;
import com.vnptit.nfc.activity.VnptScanNFCActivity;
import com.vnptit.nfc.utils.KeyIntentConstantsNFC;
import com.vnptit.nfc.utils.KeyResultConstantsNFC;
import com.vnptit.nfc.utils.SDKEnumNFC;

import java.util.ArrayList;
import java.util.List;


public class SampleActivity extends AppCompatActivity implements View.OnClickListener {
   public static final String EXTRA_LOG_RESULT = "extra:LOG_RESULT";
   private ActivitySampleBinding binding;
   private ActivityResultLauncher<Intent> eKYCResultLauncher;
   private ActivityResultLauncher<Intent> nfcResultLauncher;

   @Override
   protected void onCreate(@Nullable Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      binding = ActivitySampleBinding.inflate(getLayoutInflater());
      setContentView(binding.getRoot());

      nfcResultLauncher =
           registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
              if (result.getResultCode() == Activity.RESULT_OK) {
                 final Intent data = result.getData();
                 if (data == null) return;
                 /*
                  * đường dẫn ảnh mặt trước trong thẻ chip lưu trong cache
                  * [KeyResultConstantsNFC.IMAGE_AVATAR_CARD_NFC]
                  */
                 final String avatarPath = data.getStringExtra(KeyResultConstantsNFC.IMAGE_AVATAR_CARD_NFC);

                 /*
                  * chuỗi thông tin cua SDK
                  * [KeyResultConstantsNFC.CLIENT_SESSION_RESULT]
                  */
                 final String clientSession =
                      data.getStringExtra(KeyResultConstantsNFC.CLIENT_SESSION_RESULT);

                 /*
                  * kết quả NFC
                  * [KeyResultConstantsNFC.LOG_NFC]
                  */
                 final String logNFC = data.getStringExtra(KeyResultConstantsNFC.LOG_NFC);

                 /*
                  * mã hash avatar
                  * [KeyResultConstantsNFC.HASH_AVATAR]
                  */
                 final String hashAvatar = data.getStringExtra(KeyResultConstantsNFC.HASH_AVATAR);

                 /*
                  * chuỗi json string chứa thông tin post code của quê quán
                  * [KeyResultConstantsNFC.POST_CODE_ORIGINAL_LOCATION_RESULT]
                  */
                 final String postCodeOriginalLocation =
                      data.getStringExtra(KeyResultConstantsNFC.POST_CODE_ORIGINAL_LOCATION_RESULT);

                 /*
                  * chuỗi json string chứa thông tin post code của nơi thường trú
                  * [KeyResultConstantsNFC.POST_CODE_RECENT_LOCATION_RESULT]
                  */
                 final String postCodeRecentLocation =
                      data.getStringExtra(KeyResultConstantsNFC.POST_CODE_RECENT_LOCATION_RESULT);

                 /*
                  * time scan nfc
                  * [KeyResultConstantsNFC.TIME_SCAN_NFC]
                  */
                 final String timeScanNfc = data.getStringExtra(KeyResultConstantsNFC.TIME_SCAN_NFC);

                 /*
                  * kết quả check chip căn cước công dân
                  * [KeyResultConstantsNFC.DATA_GROUPS_RESULT]
                  */
                 final String dataGroupResults =
                      data.getStringExtra(KeyResultConstantsNFC.DATA_GROUPS_RESULT);

                 /*
                  * kết quả quét QRCode căn cước công dân
                  * [KeyResultConstantsNFC.QR_CODE_RESULT_NFC]
                  */
                 final String qrCodeResult = data.getStringExtra(KeyResultConstantsNFC.QR_CODE_RESULT_NFC);

                 final ArrayList<LogResult> results = new ArrayList<>();
                 addNotNullOrEmpty(results, "Avatar NFC", avatarPath);
                 addNotNullOrEmpty(results, "Client session", clientSession);
                 addNotNullOrEmpty(results, "Log NFC", logNFC);
                 addNotNullOrEmpty(results, "Hash avatar", hashAvatar);
                 addNotNullOrEmpty(results, "Postcode original location", postCodeOriginalLocation);
                 addNotNullOrEmpty(results, "Postcode recent location", postCodeRecentLocation);
                 addNotNullOrEmpty(results, "Time scan NFC", timeScanNfc);
                 addNotNullOrEmpty(results, "Data group results", dataGroupResults);
                 addNotNullOrEmpty(results, "Qrcode", qrCodeResult);

                 if (!results.isEmpty()) {
                    final Intent intent = new Intent(this, LogActivity.class);
                    intent.putParcelableArrayListExtra(EXTRA_LOG_RESULT, results);
                    startActivity(intent);
                 }
              }
           });

      eKYCResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
         if (result.getResultCode() == Activity.RESULT_OK) {
            final Intent data = result.getData();
            if (data != null) {
               final Intent intent = new Intent(this, LogActivity.class);
               final ArrayList<LogResult> results = new ArrayList<>();

               /*
                * vi: Dữ liệu bóc tách thông tin OCR
                * en: OCR info extraction
                * OCR Result
                */
               final String strOCRResult = data.getStringExtra(KeyResultConstants.INFO_RESULT);
               addNotNullOrEmpty(results, "OCR", strOCRResult);

               /*
                * vi: Dữ liệu bóc tách thông tin Liveness card mặt trớc
                * en: Liveness card front info extraction
                * LIVENESS CARD FRONT result
                */
               final String strLivenessCardFrontResult = data.getStringExtra(KeyResultConstants.LIVENESS_CARD_FRONT_RESULT);
               addNotNullOrEmpty(results, "Liveness Card Front", strLivenessCardFrontResult);

               /*
                * vi: Dữ liệu bóc tách thông tin liveness card mặt sau
                * en: Liveness card rear info extraction
                * LIVENESS CARD REAR Result
                */
               final String strLivenessCardRearResult = data.getStringExtra(KeyResultConstants.LIVENESS_CARD_REAR_RESULT);
               addNotNullOrEmpty(results, "Liveness Card Rear", strLivenessCardRearResult);

               /*
                * vi: Dữ liệu bóc tách thông tin compare face
                * en: Compare face info extraction
                * COMPARE Result
                */
               final String strCompareResult = data.getStringExtra(KeyResultConstants.COMPARE_RESULT);
               addNotNullOrEmpty(results, "Compare", strCompareResult);

               /*
                * vi: Dữ liệu bóc tách thông tin liveness face
                * en: Liveness face info extraction
                * LIVENESS FACE Result
                */
               final String strLivenessFaceResult = data.getStringExtra(KeyResultConstants.LIVENESS_FACE_RESULT);
               addNotNullOrEmpty(results, "Liveness Face", strLivenessFaceResult);

               /*
                * vi: Dữ liệu bóc tách thông tin mask face
                * en: Mask face info extraction
                * MASKED FACE Result
                */
               final String strMaskFaceResult = data.getStringExtra(KeyResultConstants.MASKED_FACE_RESULT);
               intent.putExtra(KeyResultConstants.MASKED_FACE_RESULT, strMaskFaceResult);
               addNotNullOrEmpty(results, "Mask Face", strMaskFaceResult);

               if (!results.isEmpty()) {
                  intent.putParcelableArrayListExtra(EXTRA_LOG_RESULT, results);
                  startActivity(intent);
               }
            }
         }
      });

      initListeners();
   }

   private void addNotNullOrEmpty(List<LogResult> results, String key, String value) {
      results.add(new LogResult(key, value));
   }

   private boolean isDeviceSupportedNfc() {
      final NfcAdapter adapter = NfcAdapter.getDefaultAdapter(this);
      if (adapter != null && adapter.isEnabled()) {
         return true;
      }

      Toast.makeText(this, "Thiết bị không hỗ trợ NFC", Toast.LENGTH_SHORT).show();
      return false;
   }

   @Override
   public void onClick(View view) {
      if (view == binding.ekycFull) {
         startEkycFull();
      } else if (view == binding.ekycFace) {
         startEkycFace();
      } else if (view == binding.ekycOcr) {
         startEkycOcr();
      } else if (view == binding.scanNfc) {
         if (!isDeviceSupportedNfc()) {
            return;
         }

         final MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
         final View dialogView = getLayoutInflater().inflate(R.layout.dialog_card_info, null);
         builder.setView(view);
         builder.setTitle("Nhập thông tin");
         builder.setNegativeButton(android.R.string.cancel, null);
         builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
            final TextInputLayout cardIdInput = dialogView.findViewById(R.id.card_id_input);
            final TextInputLayout cardDobInput = dialogView.findViewById(R.id.card_dob_input);
            final TextInputLayout cardExpiredDateInput = dialogView.findViewById(R.id.card_expired_date_input);

            dialog.cancel();
            navigateToScanNfc(
                 cardIdInput.getEditText().getText().toString().trim(),
                 cardDobInput.getEditText().getText().toString().trim(),
                 cardExpiredDateInput.getEditText().getText().toString().trim()
            );
         });
         builder.show();
      } else if (view == binding.nfcQrcode) {
         if (!isDeviceSupportedNfc()) {
            return;
         }

         navigateToNfcQrCode();
      }
   }

   private void initListeners() {
      binding.ekycFull.setOnClickListener(this);
      binding.ekycOcr.setOnClickListener(this);
      binding.ekycFace.setOnClickListener(this);
      binding.scanNfc.setOnClickListener(this);
      binding.nfcQrcode.setOnClickListener(this);
   }

   private void deinitListeners() {
      binding.ekycFull.setOnClickListener(null);
      binding.ekycOcr.setOnClickListener(null);
      binding.ekycFace.setOnClickListener(null);
      binding.scanNfc.setOnClickListener(null);
      binding.nfcQrcode.setOnClickListener(null);
   }

   private void navigateToNfcQrCode() {
      final Intent intent = new Intent(this, VnptScanNFCActivity.class);
      /*
       * Truyền access token chứa bearer
       */
      intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN, "<ACCESS_TOKEN> (including bearer)");
      intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN_EKYC, "<ACCESS_TOKEN> (including bearer)");
      /*
       * Truyền token id
       */
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID, "<TOKEN_ID>");
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID_EKYC, "<TOKEN_ID>");
      /*
       * Truyền token key
       */
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY, "<TOKEN_KEY>");
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY_EKYC, "<TOKEN_KEY>");
      /*
       * điều chỉnh ngôn ngữ tiếng việt
       *    - vi: tiếng việt
       *    - en: tiếng anh
       */
      intent.putExtra(KeyIntentConstantsNFC.LANGUAGE_NFC, SDKEnumNFC.LanguageEnum.VIETNAMESE.getValue());
      /*
       * hiển thị màn hình hướng dẫn + hiển thị nút bỏ qua hướng dẫn
       * - mặc định luôn luôn hiển thị màn hình hướng dẫn
       *    - true: hiển thị nút bỏ qua
       *    - false: ko hiển thị nút bỏ qua
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_GOT_IT, true);
      /*
       * bật tính năng upload ảnh
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_UPLOAD_IMAGE, true);
      /*
       * bật tính năng get Postcode
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_MAPPING_ADDRESS, true);
      /*
       * bật tính năng xác thực chip
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_CHECK_CHIP_CLONE, true);
      /*
       * truyền các giá trị đọc thẻ
       *    - nếu không truyền gì mặc định sẽ đọc tất cả (MRZ,Verify Document,Image Avatar)
       *    - giá trị truyền vào là 1 mảng int: nếu muốn đọc giá trị nào sẽ truyền
       *      giá trị đó vào mảng
       * eg: chỉ đọc thông tin MRZ
       *    intArrayOf(SDKEnumNFC.ReadingNFCTags.MRZInfo.value)
       */
      intent.putExtra(
           KeyIntentConstantsNFC.READING_TAG_NFC,
           new int[]{
                SDKEnumNFC.ReadingNFCTags.MRZInfo.getValue(),
                SDKEnumNFC.ReadingNFCTags.VerifyDocumentInfo.getValue(),
                SDKEnumNFC.ReadingNFCTags.ImageAvatarInfo.getValue()
           }
      );
      /*
       * truyền giá trị READER_CARD_MODE
       *    - NONE: tắt
       *    - QRCode: bật quét QRCode
       *    - MRZ_CODE: bật quét MRZ
       */
      intent.putExtra(KeyIntentConstantsNFC.READER_CARD_MODE, SDKEnumNFC.ReaderCardMode.NONE.getValue());
      // set baseDomain="" => sử dụng mặc định là Product
      intent.putExtra(KeyIntentConstantsNFC.CHANGE_BASE_URL_NFC, "");

      nfcResultLauncher.launch(intent);
   }

   private void navigateToScanNfc(String id, String dob, String expiredDate) {
      final Intent intent = new Intent(this, VnptScanNFCActivity.class);
      /*
       * Truyền access token chứa bearer
       */
      intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN, "<ACCESS_TOKEN> (including bearer)");
      intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN_EKYC, "<ACCESS_TOKEN> (including bearer)");
      /*
       * Truyền token id
       */
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID, "<TOKEN_ID>");
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID_EKYC, "<TOKEN_ID>");
      /*
       * Truyền token key
       */
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY, "<TOKEN_KEY>");
      intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY_EKYC, "<TOKEN_KEY>");
      /*
       * điều chỉnh ngôn ngữ tiếng việt
       *    - vi: tiếng việt
       *    - en: tiếng anh
       */
      intent.putExtra(KeyIntentConstantsNFC.LANGUAGE_NFC, SDKEnumNFC.LanguageEnum.VIETNAMESE.getValue());
      /*
       * hiển thị màn hình hướng dẫn + hiển thị nút bỏ qua hướng dẫn
       * - mặc định luôn luôn hiển thị màn hình hướng dẫn
       *    - true: hiển thị nút bỏ qua
       *    - false: ko hiển thị nút bỏ qua
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_GOT_IT, true);
      /*
       * bật tính năng upload ảnh
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_UPLOAD_IMAGE, true);
      /*
       * bật tính năng get Postcode
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_MAPPING_ADDRESS, true);
      /*
       * bật tính năng xác thực chip
       *    - true: bật tính năng
       *    - false: tắt tính năng
       */
      intent.putExtra(KeyIntentConstantsNFC.IS_CHECK_CHIP_CLONE, true);
      /*
       * truyền các giá trị đọc thẻ
       *    - nếu không truyền gì mặc định sẽ đọc tất cả (MRZ,Verify Document,Image Avatar)
       *    - giá trị truyền vào là 1 mảng int: nếu muốn đọc giá trị nào sẽ truyền
       *      giá trị đó vào mảng
       * eg: chỉ đọc thông tin MRZ
       *    intArrayOf(SDKEnumNFC.ReadingNFCTags.MRZInfo.value)
       */
      intent.putExtra(
           KeyIntentConstantsNFC.READING_TAG_NFC,
           new int[]{
                SDKEnumNFC.ReadingNFCTags.MRZInfo.getValue(),
                SDKEnumNFC.ReadingNFCTags.VerifyDocumentInfo.getValue(),
                SDKEnumNFC.ReadingNFCTags.ImageAvatarInfo.getValue()
           }
      );
      /*
       * truyền giá trị READER_CARD_MODE
       *    - NONE: tắt
       *    - QRCode: bật quét QRCode
       *    - MRZ_CODE: bật quét MRZ
       */
      intent.putExtra(KeyIntentConstantsNFC.READER_CARD_MODE, SDKEnumNFC.ReaderCardMode.QRCODE.getValue());
      // set baseDomain="" => sử dụng mặc định là Product
      intent.putExtra(KeyIntentConstantsNFC.CHANGE_BASE_URL_NFC, "");
      // truyền id định danh căn cước công dân
      intent.putExtra(KeyIntentConstantsNFC.ID_NUMBER_CARD, id);
      // truyền ngày sinh ghi trên căn cước công dân
      intent.putExtra(KeyIntentConstantsNFC.BIRTHDAY_CARD, dob);
      // truyền ngày hết hạn căn cước công dân
      intent.putExtra(KeyIntentConstantsNFC.EXPIRED_CARD, expiredDate);

      nfcResultLauncher.launch(intent);
   }

   // Phương thức thực hiện eKYC luồng đầy đủ bao gồm: Chụp ảnh giấy tờ và chụp ảnh chân dung
   // Bước 1 - chụp ảnh chân dung xa gần
   // Bước 2 - hiển thị kết quả
   private void startEkycFace() {
      final Intent intent = getBaseIntent(VnptPortraitActivity.class);

      // Giá trị này xác định phiên bản khi sử dụng Máy ảnh tại bước chụp ảnh chân dung luồng full. Mặc định là Normal ✓
      // - Normal: chụp ảnh chân dung 1 hướng
      // - ADVANCED: chụp ảnh chân dung xa gần
      intent.putExtra(KeyIntentConstants.VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

      // Bật/[Tắt] chức năng So sánh ảnh trong thẻ và ảnh chân dung
      intent.putExtra(KeyIntentConstants.IS_COMPARE_FLOW, false);

      // Bật/Tắt chức năng kiểm tra che mặt
      intent.putExtra(KeyIntentConstants.IS_CHECK_MASKED_FACE, true);

      // Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
      // - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
      // - IBeta: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
      // - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
      intent.putExtra(KeyIntentConstants.CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

      eKYCResultLauncher.launch(intent);
   }


   // Phương thức thực hiện eKYC luồng "Chụp ảnh giấy tờ"
   // Bước 1 - chụp ảnh giấy tờ
   // Bước 2 - hiển thị kết quả
   private void startEkycOcr() {
      final Intent intent = getBaseIntent(VnptOcrActivity.class);

      // Giá trị này xác định kiểu giấy tờ để sử dụng:
      // - IdentityCard: Chứng minh thư nhân dân, Căn cước công dân
      // - IDCardChipBased: Căn cước công dân gắn Chip
      // - Passport: Hộ chiếu
      // - DriverLicense: Bằng lái xe
      // - MilitaryIdCard: Chứng minh thư quân đội
      intent.putExtra(KeyIntentConstants.DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

      // Bật/Tắt chức năng kiểm tra ảnh giấy tờ chụp trực tiếp (liveness card)
      intent.putExtra(KeyIntentConstants.IS_CHECK_LIVENESS_CARD, true);

      // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
      // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
      // - Basic: Kiểm tra sau khi chụp ảnh
      // - MediumFlip: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
      // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
      intent.putExtra(KeyIntentConstants.TYPE_VALIDATE_DOCUMENT, SDKEnum.TypeValidateDocument.Basic.getValue());

      eKYCResultLauncher.launch(intent);
   }


   // Phương thức thực hiện eKYC luồng đầy đủ bao gồm: Chụp ảnh giấy tờ và chụp ảnh chân dung
   // Bước 1 - chụp ảnh giấy tờ
   // Bước 2 - chụp ảnh chân dung xa gần
   // Bước 3 - hiển thị kết quả
   private void startEkycFull() {
      final Intent intent = getBaseIntent(VnptIdentityActivity.class);

      // Giá trị này xác định kiểu giấy tờ để sử dụng:
      // - IDENTITY_CARD: Chứng minh thư nhân dân, Căn cước công dân
      // - IDCardChipBased: Căn cước công dân gắn Chip
      // - Passport: Hộ chiếu
      // - DriverLicense: Bằng lái xe
      // - MilitaryIdCard: Chứng minh thư quân đội
      intent.putExtra(KeyIntentConstants.DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

      // Bật/Tắt chức năng So sánh ảnh trong thẻ và ảnh chân dung
      intent.putExtra(KeyIntentConstants.IS_COMPARE_FLOW, true);

      // Bật/Tắt chức năng kiểm tra ảnh giấy tờ chụp trực tiếp (liveness card)
      intent.putExtra(KeyIntentConstants.IS_CHECK_LIVENESS_CARD, true);

      // Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
      // - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
      // - iBETA: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
      // - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
      intent.putExtra(KeyIntentConstants.CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

      // Bật/Tắt chức năng kiểm tra che mặt
      intent.putExtra(KeyIntentConstants.IS_CHECK_MASKED_FACE, true);

      // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
      // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
      // - Basic: Kiểm tra sau khi chụp ảnh
      // - MediumFlip: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
      // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
      intent.putExtra(KeyIntentConstants.TYPE_VALIDATE_DOCUMENT, SDKEnum.TypeValidateDocument.Basic.getValue());

      // Giá trị này xác định việc có xác thực số ID với mã tỉnh thành, quận huyện, xã phường tương ứng hay không.
      intent.putExtra(KeyIntentConstants.IS_VALIDATE_POSTCODE, true);

      // Giá trị này xác định phiên bản khi sử dụng Máy ảnh tại bước chụp ảnh chân dung luồng full. Mặc định là Normal ✓
      // - Normal: chụp ảnh chân dung 1 hướng
      // - ProOval: chụp ảnh chân dung xa gần
      intent.putExtra(KeyIntentConstants.VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

      eKYCResultLauncher.launch(intent);
   }

   private Intent getBaseIntent(final Class<?> clazz) {
      final Intent intent = new Intent(this, clazz);

      // Nhập thông tin bộ mã truy cập. Lấy tại mục Quản lý Token https://ekyc.vnpt.vn/admin-dashboard/console/project-manager
      intent.putExtra(KeyIntentConstants.ACCESS_TOKEN, "bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJkYTVkMjYwNy1hMGJlLTExZWItODE4Zi03ZDJjNGZlNDA1ZWQiLCJhdWQiOlsicmVzdHNlcnZpY2UiXSwidXNlcl9uYW1lIjoicGh1bmdwbkB2bnB0LnZuIiwic2NvcGUiOlsicmVhZCJdLCJpc3MiOiJodHRwczovL2xvY2FsaG9zdCIsIm5hbWUiOiJwaHVuZ3BuQHZucHQudm4iLCJ1dWlkX2FjY291bnQiOiJkYTVkMjYwNy1hMGJlLTExZWItODE4Zi03ZDJjNGZlNDA1ZWQiLCJhdXRob3JpdGllcyI6WyJVU0VSIl0sImp0aSI6Ijg1ODJkNTIxLTAxZTAtNDU3ZS05Nzc5LTBmNTA4MmYyN2ViNSIsImNsaWVudF9pZCI6ImFkbWluYXBwIn0.8EFz7oBfQkof7VzmXFEWYtCUUtz1YPjv0Xgy6ouYua1QuhkxyV2wX8gLc4t8zV-ir6vsJ28xHWY_evOO6MvRd8E9PRExcOBUebbqJQ1xmkjCx4eLWKhyhymT0mW88kzWj87wNhDZhL9-PQ2MlAmQjohi3YVkAMJdUjUJJQGJ-CIds9BD_OkGS8YlAs_Lfp2_3hxRTPdV85v9o5RqEh9mIJMIk1Mh4BcnuJSfihqNfIoSg1p27WFvjfyk2_61CgLMC3Iqc6WT92j1qc6tjVgPSnPnVrv0cfKdHWmcb48AaSSacqlYhIhHzE30Y_P_ESK3wAILKLdsTJ_CZQ7z9Z4Syg");
      intent.putExtra(KeyIntentConstants.TOKEN_ID, "c0503c5a-8160-afcf-e053-604fc10a2953");
      intent.putExtra(KeyIntentConstants.TOKEN_KEY, "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM2YUe2wcFngILRw4Y2dmq3IMZaFaWxfc4tge31gIjSJ6QCnI0BUUFC5lC3iTnDyMt/R0WwaPmNhhZvaNVSkLzsCAwEAAQ==");

      // Giá trị này dùng để đảm bảo mỗi yêu cầu (request) từ phía khách hàng sẽ không bị thay đổi.
      intent.putExtra(KeyIntentConstants.CHALLENGE_CODE, "INNOVATIONCENTER");

      // Ngôn ngữ sử dụng trong SDK
      // - VIETNAMESE: Tiếng Việt
      // - ENGLISH: Tiếng Anh
      intent.putExtra(KeyIntentConstants.LANGUAGE_SDK, SDKEnum.LanguageEnum.VIETNAMESE.getValue());

      // Bật/Tắt Hiển thị màn hình hướng dẫn
      intent.putExtra(KeyIntentConstants.IS_SHOW_TUTORIAL, true);

      // Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video
      intent.putExtra(KeyIntentConstants.IS_ENABLE_GOT_IT, true);

      // Sử dụng máy ảnh mặt trước
      // - FRONT: Camera trước
      // - BACK: Camera trước
      intent.putExtra(KeyIntentConstants.CAMERA_POSITION_FOR_PORTRAIT, SDKEnum.CameraTypeEnum.FRONT.getValue());

      return intent;
   }

   @Override
   protected void onDestroy() {
      deinitListeners();
      super.onDestroy();
   }
}