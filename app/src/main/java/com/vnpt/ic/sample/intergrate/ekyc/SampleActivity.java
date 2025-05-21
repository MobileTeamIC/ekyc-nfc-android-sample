package com.vnpt.ic.sample.intergrate.ekyc;

import static com.vnptit.idg.sdk.utils.KeyIntentConstants.ACCESS_TOKEN;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CAMERA_POSITION_FOR_PORTRAIT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CHALLENGE_CODE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.CHECK_LIVENESS_FACE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.DOCUMENT_TYPE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.HASH_FRONT_OCR;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.HASH_IMAGE_COMPARE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_CHECK_LIVENESS_CARD;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_CHECK_MASKED_FACE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_COMPARE_GENERAL;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_ENABLE_COMPARE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_ENABLE_GOT_IT;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_SHOW_TUTORIAL;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.IS_VALIDATE_POSTCODE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.LANGUAGE_SDK;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TOKEN_ID;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.TOKEN_KEY;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.VALIDATE_DOCUMENT_TYPE;
import static com.vnptit.idg.sdk.utils.KeyIntentConstants.VERSION_SDK;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.COMPARE_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_BACK_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_CARD_FRONT_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.LIVENESS_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.MASKED_FACE_RESULT;
import static com.vnptit.idg.sdk.utils.KeyResultConstants.OCR_RESULT;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.vnpt.ic.sample.intergrate.ekyc.databinding.ActivitySampleBinding;
import com.vnptit.idg.sdk.activity.VnptFrontActivity;
import com.vnptit.idg.sdk.activity.VnptIdentityActivity;
import com.vnptit.idg.sdk.activity.VnptOcrActivity;
import com.vnptit.idg.sdk.activity.VnptPortraitActivity;
import com.vnptit.idg.sdk.activity.VnptQRCodeActivity;
import com.vnptit.idg.sdk.activity.VnptRearActivity;
import com.vnptit.idg.sdk.utils.SDKEnum;
import com.vnptit.nfc.activity.VnptScanNFCActivity;
import com.vnptit.nfc.utils.KeyIntentConstantsNFC;
import com.vnptit.nfc.utils.KeyResultConstantsNFC;
import com.vnptit.nfc.utils.SDKEnumNFC;

import java.util.ArrayList;

public class SampleActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivitySampleBinding binding;
    private ActivityResultLauncher<Intent> eKYCResultLauncher;
    private ActivityResultLauncher<Intent> resultLauncher;

    public static final String EXTRA_LOG_RESULT = "EXTRA_LOG_RESULT";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySampleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        eKYCResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                final Intent data = result.getData();
                if (data != null) {
                    final Intent intent = new Intent(this, LogActivity.class);

                    /*
                     * vi: Dữ liệu bóc tách thông tin OCR
                     * en: OCR info extraction
                     * OCR Result
                     */
                    final String strOCRResult = data.getStringExtra(OCR_RESULT);
                    intent.putExtra(OCR_RESULT, strOCRResult);

                    /*
                     * vi: Dữ liệu bóc tách thông tin Liveness card mặt trớc
                     * en: Liveness card front info extraction
                     * LIVENESS CARD FRONT result
                     */
                    final String strLivenessCardFrontResult = data.getStringExtra(LIVENESS_CARD_FRONT_RESULT);
                    intent.putExtra(LIVENESS_CARD_FRONT_RESULT, strLivenessCardFrontResult);

                    /*
                     * vi: Dữ liệu bóc tách thông tin liveness card mặt sau
                     * en: Liveness card rear info extraction
                     * LIVENESS CARD REAR Result
                     */
                    final String strLivenessCardRearResult = data.getStringExtra(LIVENESS_CARD_BACK_RESULT);
                    intent.putExtra(LIVENESS_CARD_BACK_RESULT, strLivenessCardRearResult);

                    /*
                     * vi: Dữ liệu bóc tách thông tin compare face
                     * en: Compare face info extraction
                     * COMPARE Result
                     */
                    final String strCompareResult = data.getStringExtra(COMPARE_FACE_RESULT);
                    intent.putExtra(COMPARE_FACE_RESULT, strCompareResult);

                    /*
                     * vi: Dữ liệu bóc tách thông tin liveness face
                     * en: Liveness face info extraction
                     * LIVENESS FACE Result
                     */
                    final String strLivenessFaceResult = data.getStringExtra(LIVENESS_FACE_RESULT);
                    intent.putExtra(LIVENESS_FACE_RESULT, strLivenessFaceResult);

                    /*
                     * vi: Dữ liệu bóc tách thông tin mask face
                     * en: Mask face info extraction
                     * MASKED FACE Result
                     */
                    final String strMaskFaceResult = data.getStringExtra(MASKED_FACE_RESULT);
                    intent.putExtra(MASKED_FACE_RESULT, strMaskFaceResult);

                    startActivity(intent);
                }
            }
        });

        resultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();

                    String avatarPath = data.getStringExtra(KeyResultConstantsNFC.PATH_IMAGE_AVATAR);
                    String clientSession = data.getStringExtra(KeyResultConstantsNFC.CLIENT_SESSION_RESULT);
                    String logNFC = data.getStringExtra(KeyResultConstantsNFC.DATA_NFC_RESULT);
                    String hashAvatar = data.getStringExtra(KeyResultConstantsNFC.HASH_IMAGE_AVATAR);
                    String postCodeOriginalLocation = data.getStringExtra(KeyResultConstantsNFC.POST_CODE_ORIGINAL_LOCATION_RESULT);
                    String postCodeRecentLocation = data.getStringExtra(KeyResultConstantsNFC.POST_CODE_RECENT_LOCATION_RESULT);
                    String timeScanNfc = data.getStringExtra(KeyResultConstantsNFC.TIME_SCAN_NFC);
                    String checkAuthChipResult = data.getStringExtra(KeyResultConstantsNFC.STATUS_CHIP_AUTHENTICATION);
                    String qrCodeResult = data.getStringExtra(KeyResultConstantsNFC.QR_CODE_RESULT);

                    ArrayList<LogResult> results = new ArrayList<>();
                    addNotNullOrEmpty(results, "Avatar NFC", avatarPath);
                    addNotNullOrEmpty(results, "Client session", clientSession);
                    addNotNullOrEmpty(results, "Log NFC", logNFC);
                    addNotNullOrEmpty(results, "Hash avatar", hashAvatar);
                    addNotNullOrEmpty(results, "Postcode original location", postCodeOriginalLocation);
                    addNotNullOrEmpty(results, "Postcode recent location", postCodeRecentLocation);
                    addNotNullOrEmpty(results, "Time scan NFC", timeScanNfc);
                    addNotNullOrEmpty(results, "Check auth chip", checkAuthChipResult);
                    addNotNullOrEmpty(results, "Qrcode", qrCodeResult);

                    if (!results.isEmpty()) {
                        Intent intent = new Intent(this, LogResultActivity.class);
                        intent.putExtra(EXTRA_LOG_RESULT, results);
                        startActivity(intent);
                    }
                }
            }
        );

        initListeners();
    }

    @Override
    public void onClick(View view) {
        if (view == binding.ekycFull) {
            startEkycFull();
        } else if (view == binding.ekycFace) {
            startEkycFace();
        } else if (view == binding.ekycOcr) {
            startEkycOcr();
        } else if (view == binding.ekycOcrFront) {
            startEkycOcrFront();
        } else if (view == binding.ekycOcrBack) {
            startEkycOcrBack();
        } else if (view == binding.ekycQr) {
            startEkycQR();
        } else if (view == binding.nfcQr) {
            if (!isDeviceSupportedNfc(binding.getRoot())) return;
            navigateToNfcQrCode();
        } else if (view == binding.nfcMrz) {
            navigateToMrzNfc();
        } else if (view == binding.nfc) {
            if (!isDeviceSupportedNfc(binding.getRoot())) return;

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this);
            View dialogView = getLayoutInflater().inflate(R.layout.dialog_card_info, null);

            builder.setView(dialogView);
            builder.setTitle("Nhập thông tin");

            builder.setNegativeButton(android.R.string.cancel, (dialog, which) -> dialog.cancel());

            builder.setPositiveButton(android.R.string.ok, (dialog, which) -> {
                TextInputLayout cardIdInput = dialogView.findViewById(R.id.card_id_input);
                TextInputLayout cardDobInput = dialogView.findViewById(R.id.card_dob_input);
                TextInputLayout cardExpiredDateInput = dialogView.findViewById(R.id.card_expired_date_input);

                String id = cardIdInput.getEditText().getText().toString().trim();
                String dob = cardDobInput.getEditText().getText().toString().trim();
                String expiredDate = cardExpiredDateInput.getEditText().getText().toString().trim();

                navigateToScanNfc(id, dob, expiredDate);
                dialog.cancel();
            });

            builder.show();
        }
    }


    private void initListeners() {
        binding.ekycOcr.setOnClickListener(this);
        binding.ekycFace.setOnClickListener(this);
        binding.nfcQr.setOnClickListener(this);
        binding.nfc.setOnClickListener(this);
        binding.ekycFull.setOnClickListener(this);
        binding.ekycOcrFront.setOnClickListener(this);
        binding.ekycOcrBack.setOnClickListener(this);
        binding.ekycQr.setOnClickListener(this);
        binding.nfcMrz.setOnClickListener(this);
    }

    private void deinitListeners() {
        binding.ekycOcr.setOnClickListener(null);
        binding.ekycFace.setOnClickListener(null);
        binding.nfcQr.setOnClickListener(null);
        binding.nfc.setOnClickListener(null);
        binding.ekycFull.setOnClickListener(null);
        binding.ekycOcrFront.setOnClickListener(null);
        binding.ekycOcrBack.setOnClickListener(null);
        binding.ekycQr.setOnClickListener(null);
        binding.nfcMrz.setOnClickListener(null);

    }

//    Start EKYC part

    // Phương thức thực hiện eKYC luồng đầy đủ bao gồm: Chụp ảnh giấy tờ và chụp ảnh chân dung
    // Bước 1 - chụp ảnh chân dung xa gần
    // Bước 2 - hiển thị kết quả

    /* TODO: Add dialog to insert hash image */
    private void startEkycFace() {
        DialogUtils.showInputDialog(this, hashValue -> {
            final Intent intent = getBaseIntent(VnptPortraitActivity.class);

            // Giá trị này xác định phiên bản khi sử dụng Máy ảnh tại bước chụp ảnh chân dung luồng full. Mặc định là Normal ✓
            // - Normal: chụp ảnh chân dung 1 hướng
            // - ADVANCED: chụp ảnh chân dung xa gần
            intent.putExtra(VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

            // Bật/[Tắt] chức năng So sánh ảnh trong thẻ và ảnh chân dung
            intent.putExtra(IS_ENABLE_COMPARE, true);

            /**
             * Giá trị này xác định việc có thực hiện so sánh khuôn mặt chân dung trong giấy tờ dạng đầy đủ (ví dụ ảnh thẻ) với ảnh chân dung sau khi chụp ảnh từ SDK.
             * Mặc định là false
             */
            intent.putExtra(IS_COMPARE_GENERAL, true);

            /**
             * Giá trị này là mã ảnh chứa ảnh chân dung (có thể là ảnh mặt trước giấy tờ), được truyền vào để so sánh với ảnh chân dung sau khi chụp ảnh từ SDK.
             * Mặc định giá trị rỗng ("")
             */
            intent.putExtra(HASH_IMAGE_COMPARE, hashValue);

            // Bật/Tắt chức năng kiểm tra che mặt
            intent.putExtra(IS_CHECK_MASKED_FACE, true);

            // Lựa chọn chức năng kiểm tra ảnh chân dung chụp trực tiếp (liveness face)
            // - NoneCheckFace: Không thực hiện kiểm tra ảnh chân dung chụp trực tiếp hay không
            // - IBeta: Kiểm tra ảnh chân dung chụp trực tiếp hay không iBeta (phiên bản hiện tại)
            // - Standard: Kiểm tra ảnh chân dung chụp trực tiếp hay không Standard (phiên bản mới)
            intent.putExtra(CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

            eKYCResultLauncher.launch(intent);
        });
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
        intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

        // Bật/Tắt chức năng kiểm tra ảnh giấy tờ chụp trực tiếp (liveness card)
        intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

        // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
        // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
        // - Basic: Kiểm tra sau khi chụp ảnh
        // - Medium: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
        // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
        intent.putExtra(VALIDATE_DOCUMENT_TYPE, SDKEnum.ValidateDocumentType.Medium.getValue());

        // Bật/Tắt hướng dẫn
        intent.putExtra(IS_SHOW_TUTORIAL, true);

        //Bật/Tắt xác thực thông tin postal code
        intent.putExtra(IS_VALIDATE_POSTCODE, true);

        eKYCResultLauncher.launch(intent);
    }

    private void startEkycFull() {
        final Intent intent = getBaseIntent(VnptIdentityActivity.class);

        // Version SDK
        intent.putExtra(VERSION_SDK, SDKEnum.VersionSDKEnum.ADVANCED.getValue());

        // Bật/Tắt Hiển thị màn hình hướng dẫn
        intent.putExtra(IS_SHOW_TUTORIAL, true);

        // Giá trị này xác định kiểu giấy tờ để sử dụng:
        // - IdentityCard: Chứng minh thư nhân dân, Căn cước công dân
        // - IDCardChipBased: Căn cước công dân gắn Chip
        // - Passport: Hộ chiếu
        // - DriverLicense: Bằng lái xe
        // - MilitaryIdCard: Chứng minh thư quân đội
        intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

        // Bật/Tắt chức năng so sánh
        intent.putExtra(IS_ENABLE_COMPARE, true);

        // Bật/Tắt chức năng kiểm tra che mặt
        intent.putExtra(IS_CHECK_MASKED_FACE, true);

        // Bật/Tắt chức năng kiểm tra liveness khuôn mặt
        intent.putExtra(CHECK_LIVENESS_FACE, SDKEnum.ModeCheckLiveNessFace.iBETA.getValue());

        // Bật/Tắt chức năng xác thực thông tin postal code
        intent.putExtra(IS_VALIDATE_POSTCODE, true);

        // Bật/Tắt chức năng kiểm tra liveness giấy tờ
        intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

        // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
        // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
        // - Basic: Kiểm tra sau khi chụp ảnh
        // - Medium: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
        // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
        intent.putExtra(VALIDATE_DOCUMENT_TYPE, SDKEnum.ValidateDocumentType.Medium.getValue());

        eKYCResultLauncher.launch(intent);
    }

    private void startEkycQR() {
        final Intent intent = getBaseIntent(VnptQRCodeActivity.class);
        eKYCResultLauncher.launch(intent);
    }

    private void startEkycOcrFront() {
        final Intent intent = getBaseIntent(VnptFrontActivity.class);

        // Giá trị này xác định kiểu giấy tờ để sử dụng:
        // - IdentityCard: Chứng minh thư nhân dân, Căn cước công dân
        // - IDCardChipBased: Căn cước công dân gắn Chip
        // - Passport: Hộ chiếu
        // - DriverLicense: Bằng lái xe
        // - MilitaryIdCard: Chứng minh thư quân đội
        intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

        intent.putExtra(IS_VALIDATE_POSTCODE, true);

        intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

        // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
        // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
        // - Basic: Kiểm tra sau khi chụp ảnh
        // - Medium: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
        // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
        intent.putExtra(VALIDATE_DOCUMENT_TYPE, SDKEnum.ValidateDocumentType.Medium.getValue());

        intent.putExtra(IS_SHOW_TUTORIAL, true);

        eKYCResultLauncher.launch(intent);
    }

    private void startEkycOcrBack() {
        DialogUtils.showInputDialog(this, hashValue -> {
            final Intent intent = getBaseIntent(VnptRearActivity.class);
            // Truyền mã hash mặt trước của ảnh giấy tờ
            intent.putExtra(HASH_FRONT_OCR, hashValue);

            // Giá trị này xác định kiểu giấy tờ để sử dụng:
            // - IdentityCard: Chứng minh thư nhân dân, Căn cước công dân
            // - IDCardChipBased: Căn cước công dân gắn Chip
            // - Passport: Hộ chiếu
            // - DriverLicense: Bằng lái xe
            // - MilitaryIdCard: Chứng minh thư quân đội
            intent.putExtra(DOCUMENT_TYPE, SDKEnum.DocumentTypeEnum.IDENTITY_CARD.getValue());

            // Bật/Tắt chức năng kiểm tra
            intent.putExtra(IS_VALIDATE_POSTCODE, true);

            // Bật/Tắt chức năng kiểm tra liveness giấy tờ
            intent.putExtra(IS_CHECK_LIVENESS_CARD, true);

            // Lựa chọn chế độ kiểm tra ảnh giấy tờ ngay từ SDK
            // - None: Không thực hiện kiểm tra ảnh khi chụp ảnh giấy tờ
            // - Basic: Kiểm tra sau khi chụp ảnh
            // - Medium: Kiểm tra ảnh hợp lệ trước khi chụp (lật giấy tờ thành công → hiển thị nút chụp)
            // - Advance: Kiểm tra ảnh hợp lệ trước khi chụp (hiển thị nút chụp)
            intent.putExtra(VALIDATE_DOCUMENT_TYPE, SDKEnum.ValidateDocumentType.Medium.getValue());

            // Bật/Tắt hướng dẫn
            intent.putExtra(IS_SHOW_TUTORIAL, true);

            eKYCResultLauncher.launch(intent);
        });
    }


    // Base intent cho ekyc
    private Intent getBaseIntent(final Class<?> clazz) {
        final Intent intent = new Intent(this, clazz);

        // Nhập thông tin bộ mã truy cập. Lấy tại mục Quản lý Token https://ekyc.vnpt.vn/admin-dashboard/console/project-manager
        intent.putExtra(ACCESS_TOKEN, AppCode.ACCESS_TOKEN);
        intent.putExtra(TOKEN_ID, AppCode.TOKEN_ID);
        intent.putExtra(TOKEN_KEY, AppCode.TOKEN_KEY);

        // Giá trị này dùng để đảm bảo mỗi yêu cầu (request) từ phía khách hàng sẽ không bị thay đổi.
        intent.putExtra(CHALLENGE_CODE, "INNOVATIONCENTER");

        // Ngôn ngữ sử dụng trong SDK
        // - VIETNAMESE: Tiếng Việt
        // - ENGLISH: Tiếng Anh
        intent.putExtra(LANGUAGE_SDK, SDKEnum.LanguageEnum.VIETNAMESE.getValue());

        // Bật/Tắt Hiển thị màn hình hướng dẫn
        intent.putExtra(IS_SHOW_TUTORIAL, true);

        // Bật chức năng hiển thị nút bấm "Bỏ qua hướng dẫn" tại các màn hình hướng dẫn bằng video
        intent.putExtra(IS_ENABLE_GOT_IT, true);

        // Sử dụng máy ảnh mặt trước
        // - FRONT: Camera trước
        // - BACK: Camera trước
        intent.putExtra(CAMERA_POSITION_FOR_PORTRAIT, SDKEnum.CameraTypeEnum.FRONT.getValue());

        return intent;
    }

    public static void addNotNullOrEmpty(ArrayList<LogResult> list, String key, @Nullable String value) {
        if (!TextUtils.isEmpty(value)) {
            list.add(new LogResult(key, value));
        }
    }
// End EKYC part

    // Start NFC part
    private boolean isDeviceSupportedNfc(LinearLayout rootView) {
        NfcManager nfcManager = (NfcManager) getSystemService(Context.NFC_SERVICE);
        NfcAdapter adapter = nfcManager != null ? nfcManager.getDefaultAdapter() : null;

        if (adapter != null && adapter.isEnabled()) {
            return true;
        }

        Snackbar snackbar = Snackbar.make(rootView, "Thiết bị không hỗ trợ NFC", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(android.R.string.ok, v -> snackbar.dismiss());
        snackbar.show();
        return false;
    }


    private void navigateToNfcQrCode() {
        Intent intent = new Intent(this, VnptScanNFCActivity.class);

        // Truyền access token chứa bearer
        intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN, AppCode.ACCESS_TOKEN_NFC);

        // Truyền token id
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID, AppCode.TOKEN_ID_NFC);

        // Truyền token key
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY, AppCode.TOKEN_KEY_NFC);

        // Truyền access token ekyc cho dịch vụ postcode
        intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN_EKYC, AppCode.ACCESS_TOKEN);

        // Truyền token id ekyc cho dịch vụ postcode
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID_EKYC, TOKEN_ID);

        // Truyền token key ekyc cho dịch vụ postcode
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY_EKYC, TOKEN_KEY);

        // Ngôn ngữ
        intent.putExtra(KeyIntentConstantsNFC.LANGUAGE_SDK, SDKEnumNFC.LanguageEnum.VIETNAMESE.getValue());

        // Hiển thị hướng dẫn
        intent.putExtra(KeyIntentConstantsNFC.IS_SHOW_TUTORIAL, true);

        // Hiển thị nút bỏ qua
        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_GOT_IT, true);

        // Bật upload ảnh
        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_UPLOAD_IMAGE, true);

        // Bật postcode
        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_POSTCODE_MATCHING, true);

//        // Bật xác thực chip
//        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_CHECK_CHIP_CLONE, true);

//        // Đọc các tag NFC
//        int[] readingTags = new int[]{
//            SDKEnumNFC.ReadingNFCTags.MRZInfo.getValue(),
//            SDKEnumNFC.ReadingNFCTags.VerifyDocumentInfo.getValue(),
//            SDKEnumNFC.ReadingNFCTags.ImageAvatarInfo.getValue(),
//            SDKEnumNFC.ReadingNFCTags.AuthenticationInfo.getValue()
//        };
//        intent.putExtra(KeyIntentConstantsNFC.READING_TAGS_NFC, readingTags);

        // Bật chế độ quét QRCode trước khi scan NFC
        intent.putExtra(KeyIntentConstantsNFC.READER_CARD_MODE, SDKEnumNFC.ReaderCardMode.QRCODE.getValue());

        // BASE_URL
        intent.putExtra(KeyIntentConstantsNFC.BASE_URL, "");

        // Challenge code
//        intent.putExtra(KeyIntentConstantsNFC.CHALLENGE_CODE, "INNOVATIONCENTER");

        // Upload thông tin NFC
        intent.putExtra(KeyIntentConstantsNFC.TRANSACTION_PARTNER_ID_UPLOAD_NFC, "UPLOAD_NFC");

        // Địa chỉ thường trú
        intent.putExtra(KeyIntentConstantsNFC.TRANSACTION_PARTNER_ID_ORIGINAL_LOCATION, "ORIGINAL_LOCATION");

        // Địa chỉ tạm trú
        intent.putExtra(KeyIntentConstantsNFC.TRANSACTION_PARTNER_ID_RECENT_LOCATION, "RECENT_LOCATION");

        intent.putExtra(KeyIntentConstantsNFC.IS_SHOW_LOGO, true);

        resultLauncher.launch(intent);
    }

    private void navigateToMrzNfc() {
        Intent intent = new Intent(this, VnptScanNFCActivity.class);

        // Truyền access token chứa bearer
        intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN, AppCode.ACCESS_TOKEN_NFC);

        // Truyền token id
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID, AppCode.TOKEN_ID_NFC);

        // Truyền token key
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY, AppCode.TOKEN_KEY_NFC);

        // Truyền access token ekyc cho dịch vụ postcode
        intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN_EKYC, AppCode.ACCESS_TOKEN);

        // Truyền token id ekyc cho dịch vụ postcode
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID_EKYC, AppCode.TOKEN_ID);

        // Truyền token key ekyc cho dịch vụ postcode
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY_EKYC, TOKEN_KEY);

        // Điều chỉnh ngôn ngữ tiếng Việt
        intent.putExtra(KeyIntentConstantsNFC.LANGUAGE_SDK, SDKEnumNFC.LanguageEnum.VIETNAMESE.getValue());

        // Hiển thị màn hình hướng dẫn + hiển thị nút bỏ qua hướng dẫn
        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_GOT_IT, true);

        // Bật tính năng upload ảnh
        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_UPLOAD_IMAGE, true);

        // Bật tính năng get Postcode
        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_POSTCODE_MATCHING, true);

        // Chế độ đọc thẻ MRZ
        intent.putExtra(KeyIntentConstantsNFC.READER_CARD_MODE, SDKEnumNFC.ReaderCardMode.MRZ_CODE.getValue());

        // BASE_URL
        intent.putExtra(KeyIntentConstantsNFC.BASE_URL, "");

        // Hiển thị logo
        intent.putExtra(KeyIntentConstantsNFC.IS_SHOW_LOGO, true);

        resultLauncher.launch(intent);
    }


    private void navigateToScanNfc(String id, String dob, String expiredDate) {
        Intent intent = new Intent(this, VnptScanNFCActivity.class);

        // Truyền access token chứa bearer
        intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN, AppCode.ACCESS_TOKEN_NFC);

        // Truyền token id
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID, AppCode.TOKEN_ID_NFC);

        // Truyền token key
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY, AppCode.TOKEN_KEY_NFC);

        // Truyền access token ekyc cho dịch vụ postcode
        intent.putExtra(KeyIntentConstantsNFC.ACCESS_TOKEN_EKYC, AppCode.ACCESS_TOKEN);

        // Truyền token id ekyc cho dịch vụ postcode
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_ID_EKYC, TOKEN_ID);

        // Truyền token key ekyc cho dịch vụ postcode
        intent.putExtra(KeyIntentConstantsNFC.TOKEN_KEY_EKYC, TOKEN_KEY);

        // Ngôn ngữ
        intent.putExtra(KeyIntentConstantsNFC.LANGUAGE_SDK, SDKEnumNFC.LanguageEnum.VIETNAMESE.getValue());

        // Hiển thị nút bỏ qua
        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_GOT_IT, true);

        // Bật tính năng upload ảnh
        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_UPLOAD_IMAGE, true);

        // Bật tính năng get Postcode
        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_POSTCODE_MATCHING, true);

        // Bật tính năng xác thực chip
//        intent.putExtra(KeyIntentConstantsNFC.IS_ENABLE_CHECK_CHIP_CLONE, true);

        // Truyền các giá trị đọc thẻ
//        int[] readingTags = new int[]{
//            SDKEnumNFC.ReadingNFCTags.MRZInfo.getValue(),
//            SDKEnumNFC.ReadingNFCTags.VerifyDocumentInfo.getValue(),
//            SDKEnumNFC.ReadingNFCTags.ImageAvatarInfo.getValue(),
//            SDKEnumNFC.ReadingNFCTags.AuthenticationInfo.getValue()
//        };
//        intent.putExtra(KeyIntentConstantsNFC.READING_TAGS_NFC, readingTags);

        // Bật chế độ đọc card mode
        intent.putExtra(KeyIntentConstantsNFC.READER_CARD_MODE, SDKEnumNFC.ReaderCardMode.NONE.getValue());

        // BASE_URL mặc định
        intent.putExtra(KeyIntentConstantsNFC.BASE_URL, "");

        // Truyền id định danh căn cước công dân
        intent.putExtra(KeyIntentConstantsNFC.ID_NUMBER_CARD, id);

        // Truyền ngày sinh ghi trên căn cước công dân
        intent.putExtra(KeyIntentConstantsNFC.BIRTHDAY_CARD, dob);

        // Truyền ngày hết hạn căn cước công dân
        intent.putExtra(KeyIntentConstantsNFC.EXPIRED_DATE_CARD, expiredDate);

        // Truyền giá trị challenge code
//        intent.putExtra(KeyIntentConstantsNFC.CHALLENGE_CODE, "INNOVATIONCENTER");

        // Upload thông tin NFC
        intent.putExtra(KeyIntentConstantsNFC.TRANSACTION_PARTNER_ID_UPLOAD_NFC, "UPLOAD_NFC");

        // Địa chỉ thường trú
        intent.putExtra(KeyIntentConstantsNFC.TRANSACTION_PARTNER_ID_ORIGINAL_LOCATION, "ORIGINAL_LOCATION");

        // Địa chỉ tạm trú
        intent.putExtra(KeyIntentConstantsNFC.TRANSACTION_PARTNER_ID_RECENT_LOCATION, "RECENT_LOCATION");

        // Hiển thị logo
        intent.putExtra(KeyIntentConstantsNFC.IS_SHOW_LOGO, true);

        resultLauncher.launch(intent);
    }

//    End NFC part

    @Override
    protected void onDestroy() {
        deinitListeners();
        super.onDestroy();
    }
}