package ua.com.radiokot.license.service.util

import org.junit.Assert
import org.junit.Test
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

internal class RsaPemEncodingTest {
    @Test
    fun encodePublicSuccessfully() {
        val publicKey: RSAPublicKey = KeyFactory.getInstance("RSA").generatePublic(
            X509EncodedKeySpec(
                Base64.getMimeDecoder().decode(
                    """
                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3XA6TWmfQv1sHjiP0JDg
                jbbhnNF3Je3YhZbdBB763uJpkbPl0pO2AshekMhn8ALPJ4sykyYBQsEuIKOroypi
                ycdK7sZzl9EgrD6eWvxLtckAVSuCR1AreVCVDfdu2Dzb5V4UF1JI2yAdNtxs3Wl4
                8af4Cjzy8YxM4Ah8VjzdOWZjFVb5A8oVkzOXYQiZQxpcWPHWAFZ5GiY9wisNaFHn
                boHndxaQ6iddFJLg2BmAToEpYH206qDyS6vIeEAUVE4/8IT3+JbJEns5dLKkPuO2
                YTmoHzADzf/r3y3vZrsg6Q9es8/Cw3K8dKFRlWTOC7c9L2sooBc403F53RYH5mBq
                xwIDAQAB
            """.trimIndent()
                )
            )
        ) as RSAPublicKey

        val encodedPublicKey = RsaPemEncoding().encodePublic(publicKey)

        Assert.assertEquals(
            """
                -----BEGIN PUBLIC KEY-----
                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3XA6TWmfQv1sHjiP0JDg
                jbbhnNF3Je3YhZbdBB763uJpkbPl0pO2AshekMhn8ALPJ4sykyYBQsEuIKOroypi
                ycdK7sZzl9EgrD6eWvxLtckAVSuCR1AreVCVDfdu2Dzb5V4UF1JI2yAdNtxs3Wl4
                8af4Cjzy8YxM4Ah8VjzdOWZjFVb5A8oVkzOXYQiZQxpcWPHWAFZ5GiY9wisNaFHn
                boHndxaQ6iddFJLg2BmAToEpYH206qDyS6vIeEAUVE4/8IT3+JbJEns5dLKkPuO2
                YTmoHzADzf/r3y3vZrsg6Q9es8/Cw3K8dKFRlWTOC7c9L2sooBc403F53RYH5mBq
                xwIDAQAB
                -----END PUBLIC KEY-----
            """.trimIndent(),
            encodedPublicKey
        )
    }

    @Test
    fun encodePrivateSuccessfully() {
        val privateKey = KeyFactory.getInstance("RSA")
            .generatePrivate(
                PKCS8EncodedKeySpec(
                    Base64.getMimeDecoder().decode(
                        """
                MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQClXWxkUs012mrb
                xN/Jt1MiwMr32QRS8DhhmxFop007Ny3iKxW9zZqu6gixOElUToDzF6djPuf9eqWY
                LCtFgMTCTLe/eW7b1s95Hea33Xl+xtVbn+UXdTvopqCGcJCXI/Ux6twnbZxsg2Nu
                uSxQU7/inKIa+Fr0ZDcHq2Kc9DI6L49lmiPaRpvULcfHnyMcRxdom9wj4ejUjpb1
                YX/ueUqV+rDxugI9QegPcoumI9GIc33uGy+8ErHZ7jmbdY1POCukxAh8JqqQ4PyG
                wSIRYCLV2OT8bnAhDld0fVMTri05SWq160XX7u2I8RggCl0FwiMVo9OQGC9ceZP9
                UMF7r4RvAgMBAAECggEARyUekTseluDVwJ+/OGXrGrSfnmp79cLsPvIcV05DaAqh
                bwDiZbqjpkWCX9y7j84GpI+0sHpMDT5LKGE4bLqt7HpdQ7/W9jZBJPa8YLakouqg
                uL7eLW1+zOWDgtPDIYOS5lsJRN1fwz/z85sveY/OqoyBIeFgIG1NT7toveZCBc3n
                CijR9z+q9G6njOUvxB6BfNyrxlaNCGEGt9dRmNzYR6Wfivdmloygx1rbQlrOhXg+
                1U+rIWAR6Hem4HPvGgeXx5/VmLzqWKnH/hp5s9KD/TfOY+vDN4qs1IwOapENkaen
                ocz3nmgI/KFFGm6rWT6CchyPt3aGXYVJH8sQogCbIQKBgQDcXJyMvtfueTYyqQOR
                5UpopRgGATNQrj8953IhC/dPhk1KNS4V4mAaxJVD+MCXRaLtQft4Js3am1Thmngf
                DKiMOSMMdycaAkqxggzvlwUHips2XcLBisbXi52K3qBdNlx5Fk4XqyHgI1d82MYH
                8YJmQVLufyERgD/Z1mrKoQ2ohQKBgQDAG9cqX3CLsDmuEtWPUH9WX5Y1CpUkRWpE
                Yc8uN9S8vIes0shEsnMvkSQ4VybcRQFDhT3kiMh1+28veRDBaJjUtHLHIu/rlwLy
                u3dLhBZAOx4y2m3gv1Y9teBx92RkO9LR8RFrOtvgESRb3wnhvIaq/fHMu8f11pl4
                vB+9t6XFYwKBgCCt302EE9O6yA3VQMFHJrTEHv7s+KYzYQ+WHjfGZYO2oAmsP0xh
                iO0PUC+HWVBVtGbJDTjxUD/mHz1hwgSgxPIemxMDLJpP0rHmXnK9i7JlnMUAPJyl
                Lv3SJ6frgg6wvB/87dHWwCxUMWpDX4LYDwWJFHpoAghtY4qzoJyyK6tdAoGARGwm
                EJN5lGcpxBQ1OBwlnm/hIfco84g9tylXD1EXVjmX4TgZ2E3utKxvSBnSX3W8Y34b
                c0A8lbj9+tyV2S0V8fxrPP32keRxSl72uFDNVAc6VEZ0CC5d2xDiZfUFLqYYCmpg
                F98Y3Q998bLY5Cuvk0trSc5ABBFxDA2FCJ9WTZ0CgYBw8u7qfs6vFRQdcJJnCvkC
                DI3Z1SdJisijiiQVD1yaivVrC6jSBg0ufz1H3aR1gEzFvRIGAFdifB2VRiXquSkG
                PuYVnO/11HLnMgrgbCm0p6xGroJJYGY4w3C+80etsO61nXaVdOLZS5f535CHPYXd
                oXEDDMnbblI4/6fKsuzlEQ==
            """.trimIndent()
                    )
                )
            ) as RSAPrivateKey

        val encodedPrivateKey = RsaPemEncoding().encodePrivate(privateKey)

        Assert.assertEquals(
            """
                -----BEGIN RSA PRIVATE KEY-----
                MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQClXWxkUs012mrb
                xN/Jt1MiwMr32QRS8DhhmxFop007Ny3iKxW9zZqu6gixOElUToDzF6djPuf9eqWY
                LCtFgMTCTLe/eW7b1s95Hea33Xl+xtVbn+UXdTvopqCGcJCXI/Ux6twnbZxsg2Nu
                uSxQU7/inKIa+Fr0ZDcHq2Kc9DI6L49lmiPaRpvULcfHnyMcRxdom9wj4ejUjpb1
                YX/ueUqV+rDxugI9QegPcoumI9GIc33uGy+8ErHZ7jmbdY1POCukxAh8JqqQ4PyG
                wSIRYCLV2OT8bnAhDld0fVMTri05SWq160XX7u2I8RggCl0FwiMVo9OQGC9ceZP9
                UMF7r4RvAgMBAAECggEARyUekTseluDVwJ+/OGXrGrSfnmp79cLsPvIcV05DaAqh
                bwDiZbqjpkWCX9y7j84GpI+0sHpMDT5LKGE4bLqt7HpdQ7/W9jZBJPa8YLakouqg
                uL7eLW1+zOWDgtPDIYOS5lsJRN1fwz/z85sveY/OqoyBIeFgIG1NT7toveZCBc3n
                CijR9z+q9G6njOUvxB6BfNyrxlaNCGEGt9dRmNzYR6Wfivdmloygx1rbQlrOhXg+
                1U+rIWAR6Hem4HPvGgeXx5/VmLzqWKnH/hp5s9KD/TfOY+vDN4qs1IwOapENkaen
                ocz3nmgI/KFFGm6rWT6CchyPt3aGXYVJH8sQogCbIQKBgQDcXJyMvtfueTYyqQOR
                5UpopRgGATNQrj8953IhC/dPhk1KNS4V4mAaxJVD+MCXRaLtQft4Js3am1Thmngf
                DKiMOSMMdycaAkqxggzvlwUHips2XcLBisbXi52K3qBdNlx5Fk4XqyHgI1d82MYH
                8YJmQVLufyERgD/Z1mrKoQ2ohQKBgQDAG9cqX3CLsDmuEtWPUH9WX5Y1CpUkRWpE
                Yc8uN9S8vIes0shEsnMvkSQ4VybcRQFDhT3kiMh1+28veRDBaJjUtHLHIu/rlwLy
                u3dLhBZAOx4y2m3gv1Y9teBx92RkO9LR8RFrOtvgESRb3wnhvIaq/fHMu8f11pl4
                vB+9t6XFYwKBgCCt302EE9O6yA3VQMFHJrTEHv7s+KYzYQ+WHjfGZYO2oAmsP0xh
                iO0PUC+HWVBVtGbJDTjxUD/mHz1hwgSgxPIemxMDLJpP0rHmXnK9i7JlnMUAPJyl
                Lv3SJ6frgg6wvB/87dHWwCxUMWpDX4LYDwWJFHpoAghtY4qzoJyyK6tdAoGARGwm
                EJN5lGcpxBQ1OBwlnm/hIfco84g9tylXD1EXVjmX4TgZ2E3utKxvSBnSX3W8Y34b
                c0A8lbj9+tyV2S0V8fxrPP32keRxSl72uFDNVAc6VEZ0CC5d2xDiZfUFLqYYCmpg
                F98Y3Q998bLY5Cuvk0trSc5ABBFxDA2FCJ9WTZ0CgYBw8u7qfs6vFRQdcJJnCvkC
                DI3Z1SdJisijiiQVD1yaivVrC6jSBg0ufz1H3aR1gEzFvRIGAFdifB2VRiXquSkG
                PuYVnO/11HLnMgrgbCm0p6xGroJJYGY4w3C+80etsO61nXaVdOLZS5f535CHPYXd
                oXEDDMnbblI4/6fKsuzlEQ==
                -----END RSA PRIVATE KEY-----
            """.trimIndent(),
            encodedPrivateKey
        )
    }

    @Test
    fun decodePublicSuccessfully() {
        val publicKey: RSAPublicKey = KeyFactory.getInstance("RSA").generatePublic(
            X509EncodedKeySpec(
                Base64.getMimeDecoder().decode(
                    """
                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3XA6TWmfQv1sHjiP0JDg
                jbbhnNF3Je3YhZbdBB763uJpkbPl0pO2AshekMhn8ALPJ4sykyYBQsEuIKOroypi
                ycdK7sZzl9EgrD6eWvxLtckAVSuCR1AreVCVDfdu2Dzb5V4UF1JI2yAdNtxs3Wl4
                8af4Cjzy8YxM4Ah8VjzdOWZjFVb5A8oVkzOXYQiZQxpcWPHWAFZ5GiY9wisNaFHn
                boHndxaQ6iddFJLg2BmAToEpYH206qDyS6vIeEAUVE4/8IT3+JbJEns5dLKkPuO2
                YTmoHzADzf/r3y3vZrsg6Q9es8/Cw3K8dKFRlWTOC7c9L2sooBc403F53RYH5mBq
                xwIDAQAB
            """.trimIndent()
                )
            )
        ) as RSAPublicKey

        val decodedPublicKey = RsaPemEncoding().decodePublic(
            """
                -----BEGIN PUBLIC KEY-----
                MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA3XA6TWmfQv1sHjiP0JDg
                jbbhnNF3Je3YhZbdBB763uJpkbPl0pO2AshekMhn8ALPJ4sykyYBQsEuIKOroypi
                ycdK7sZzl9EgrD6eWvxLtckAVSuCR1AreVCVDfdu2Dzb5V4UF1JI2yAdNtxs3Wl4
                8af4Cjzy8YxM4Ah8VjzdOWZjFVb5A8oVkzOXYQiZQxpcWPHWAFZ5GiY9wisNaFHn
                boHndxaQ6iddFJLg2BmAToEpYH206qDyS6vIeEAUVE4/8IT3+JbJEns5dLKkPuO2
                YTmoHzADzf/r3y3vZrsg6Q9es8/Cw3K8dKFRlWTOC7c9L2sooBc403F53RYH5mBq
                xwIDAQAB
                -----END PUBLIC KEY-----
            """.trimIndent()
        )

        Assert.assertEquals(
            publicKey,
            decodedPublicKey,
        )
    }

    @Test
    fun decodePrivateSuccessfully() {
        val privateKey = KeyFactory.getInstance("RSA")
            .generatePrivate(
                PKCS8EncodedKeySpec(
                    Base64.getMimeDecoder().decode(
                        """
                MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQClXWxkUs012mrb
                xN/Jt1MiwMr32QRS8DhhmxFop007Ny3iKxW9zZqu6gixOElUToDzF6djPuf9eqWY
                LCtFgMTCTLe/eW7b1s95Hea33Xl+xtVbn+UXdTvopqCGcJCXI/Ux6twnbZxsg2Nu
                uSxQU7/inKIa+Fr0ZDcHq2Kc9DI6L49lmiPaRpvULcfHnyMcRxdom9wj4ejUjpb1
                YX/ueUqV+rDxugI9QegPcoumI9GIc33uGy+8ErHZ7jmbdY1POCukxAh8JqqQ4PyG
                wSIRYCLV2OT8bnAhDld0fVMTri05SWq160XX7u2I8RggCl0FwiMVo9OQGC9ceZP9
                UMF7r4RvAgMBAAECggEARyUekTseluDVwJ+/OGXrGrSfnmp79cLsPvIcV05DaAqh
                bwDiZbqjpkWCX9y7j84GpI+0sHpMDT5LKGE4bLqt7HpdQ7/W9jZBJPa8YLakouqg
                uL7eLW1+zOWDgtPDIYOS5lsJRN1fwz/z85sveY/OqoyBIeFgIG1NT7toveZCBc3n
                CijR9z+q9G6njOUvxB6BfNyrxlaNCGEGt9dRmNzYR6Wfivdmloygx1rbQlrOhXg+
                1U+rIWAR6Hem4HPvGgeXx5/VmLzqWKnH/hp5s9KD/TfOY+vDN4qs1IwOapENkaen
                ocz3nmgI/KFFGm6rWT6CchyPt3aGXYVJH8sQogCbIQKBgQDcXJyMvtfueTYyqQOR
                5UpopRgGATNQrj8953IhC/dPhk1KNS4V4mAaxJVD+MCXRaLtQft4Js3am1Thmngf
                DKiMOSMMdycaAkqxggzvlwUHips2XcLBisbXi52K3qBdNlx5Fk4XqyHgI1d82MYH
                8YJmQVLufyERgD/Z1mrKoQ2ohQKBgQDAG9cqX3CLsDmuEtWPUH9WX5Y1CpUkRWpE
                Yc8uN9S8vIes0shEsnMvkSQ4VybcRQFDhT3kiMh1+28veRDBaJjUtHLHIu/rlwLy
                u3dLhBZAOx4y2m3gv1Y9teBx92RkO9LR8RFrOtvgESRb3wnhvIaq/fHMu8f11pl4
                vB+9t6XFYwKBgCCt302EE9O6yA3VQMFHJrTEHv7s+KYzYQ+WHjfGZYO2oAmsP0xh
                iO0PUC+HWVBVtGbJDTjxUD/mHz1hwgSgxPIemxMDLJpP0rHmXnK9i7JlnMUAPJyl
                Lv3SJ6frgg6wvB/87dHWwCxUMWpDX4LYDwWJFHpoAghtY4qzoJyyK6tdAoGARGwm
                EJN5lGcpxBQ1OBwlnm/hIfco84g9tylXD1EXVjmX4TgZ2E3utKxvSBnSX3W8Y34b
                c0A8lbj9+tyV2S0V8fxrPP32keRxSl72uFDNVAc6VEZ0CC5d2xDiZfUFLqYYCmpg
                F98Y3Q998bLY5Cuvk0trSc5ABBFxDA2FCJ9WTZ0CgYBw8u7qfs6vFRQdcJJnCvkC
                DI3Z1SdJisijiiQVD1yaivVrC6jSBg0ufz1H3aR1gEzFvRIGAFdifB2VRiXquSkG
                PuYVnO/11HLnMgrgbCm0p6xGroJJYGY4w3C+80etsO61nXaVdOLZS5f535CHPYXd
                oXEDDMnbblI4/6fKsuzlEQ==
            """.trimIndent()
                    )
                )
            ) as RSAPrivateKey

        val decodedPrivateKey=  RsaPemEncoding().decodePrivate(
            """
                -----BEGIN RSA PRIVATE KEY-----
                MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQClXWxkUs012mrb
                xN/Jt1MiwMr32QRS8DhhmxFop007Ny3iKxW9zZqu6gixOElUToDzF6djPuf9eqWY
                LCtFgMTCTLe/eW7b1s95Hea33Xl+xtVbn+UXdTvopqCGcJCXI/Ux6twnbZxsg2Nu
                uSxQU7/inKIa+Fr0ZDcHq2Kc9DI6L49lmiPaRpvULcfHnyMcRxdom9wj4ejUjpb1
                YX/ueUqV+rDxugI9QegPcoumI9GIc33uGy+8ErHZ7jmbdY1POCukxAh8JqqQ4PyG
                wSIRYCLV2OT8bnAhDld0fVMTri05SWq160XX7u2I8RggCl0FwiMVo9OQGC9ceZP9
                UMF7r4RvAgMBAAECggEARyUekTseluDVwJ+/OGXrGrSfnmp79cLsPvIcV05DaAqh
                bwDiZbqjpkWCX9y7j84GpI+0sHpMDT5LKGE4bLqt7HpdQ7/W9jZBJPa8YLakouqg
                uL7eLW1+zOWDgtPDIYOS5lsJRN1fwz/z85sveY/OqoyBIeFgIG1NT7toveZCBc3n
                CijR9z+q9G6njOUvxB6BfNyrxlaNCGEGt9dRmNzYR6Wfivdmloygx1rbQlrOhXg+
                1U+rIWAR6Hem4HPvGgeXx5/VmLzqWKnH/hp5s9KD/TfOY+vDN4qs1IwOapENkaen
                ocz3nmgI/KFFGm6rWT6CchyPt3aGXYVJH8sQogCbIQKBgQDcXJyMvtfueTYyqQOR
                5UpopRgGATNQrj8953IhC/dPhk1KNS4V4mAaxJVD+MCXRaLtQft4Js3am1Thmngf
                DKiMOSMMdycaAkqxggzvlwUHips2XcLBisbXi52K3qBdNlx5Fk4XqyHgI1d82MYH
                8YJmQVLufyERgD/Z1mrKoQ2ohQKBgQDAG9cqX3CLsDmuEtWPUH9WX5Y1CpUkRWpE
                Yc8uN9S8vIes0shEsnMvkSQ4VybcRQFDhT3kiMh1+28veRDBaJjUtHLHIu/rlwLy
                u3dLhBZAOx4y2m3gv1Y9teBx92RkO9LR8RFrOtvgESRb3wnhvIaq/fHMu8f11pl4
                vB+9t6XFYwKBgCCt302EE9O6yA3VQMFHJrTEHv7s+KYzYQ+WHjfGZYO2oAmsP0xh
                iO0PUC+HWVBVtGbJDTjxUD/mHz1hwgSgxPIemxMDLJpP0rHmXnK9i7JlnMUAPJyl
                Lv3SJ6frgg6wvB/87dHWwCxUMWpDX4LYDwWJFHpoAghtY4qzoJyyK6tdAoGARGwm
                EJN5lGcpxBQ1OBwlnm/hIfco84g9tylXD1EXVjmX4TgZ2E3utKxvSBnSX3W8Y34b
                c0A8lbj9+tyV2S0V8fxrPP32keRxSl72uFDNVAc6VEZ0CC5d2xDiZfUFLqYYCmpg
                F98Y3Q998bLY5Cuvk0trSc5ABBFxDA2FCJ9WTZ0CgYBw8u7qfs6vFRQdcJJnCvkC
                DI3Z1SdJisijiiQVD1yaivVrC6jSBg0ufz1H3aR1gEzFvRIGAFdifB2VRiXquSkG
                PuYVnO/11HLnMgrgbCm0p6xGroJJYGY4w3C+80etsO61nXaVdOLZS5f535CHPYXd
                oXEDDMnbblI4/6fKsuzlEQ==
                -----END RSA PRIVATE KEY-----
            """.trimIndent()
        )

        Assert.assertEquals(
            privateKey,
            decodedPrivateKey
        )
    }
}