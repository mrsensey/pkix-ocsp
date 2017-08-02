package net.klakegg.pkix.ocsp;

import net.klakegg.pkix.ocsp.util.CertificateHelper;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.security.cert.X509Certificate;

/**
 * @author erlend
 */
public class CommfidesTestOcspTest {

    private X509Certificate subjectValid =
            CertificateHelper.parse(getClass().getResourceAsStream("/commfides-test/certificate-valid.cer"));

    private X509Certificate subjectUnknown =
            CertificateHelper.parse(getClass().getResourceAsStream("/commfides-test/certificate-unknown.cer"));

    private X509Certificate issuer =
            CertificateHelper.parse(getClass().getResourceAsStream("/commfides-test/issuer.cer"));

    @Test
    public void simpleUnknown() throws OcspException {
        OcspClient ocspClient = OcspClient.builder()
                .set(OcspClient.EXCEPTION_ON_REVOKED, false)
                .set(OcspClient.EXCEPTION_ON_UNKNOWN, false)
                .build();

        OcspResponse response = ocspClient.verify(subjectUnknown, issuer);

        Assert.assertEquals(response.getStatus(), OcspStatus.UNKNOWN);
        Assert.assertNotNull(response.getThisUpdate());
        Assert.assertNull(response.getNextUpdate());
    }

    @SuppressWarnings("Duplicates")
    @Test
    public void simpleValid() throws OcspException {
        OcspClient ocspClient = OcspClient.builder()
                .build();

        OcspResponse response = ocspClient.verify(subjectValid, issuer);

        Assert.assertEquals(response.getStatus(), OcspStatus.GOOD);
        Assert.assertNotNull(response.getThisUpdate());
        Assert.assertNull(response.getNextUpdate());

        System.out.println(response);
    }
}