package com.abtd.solarbackend.quotation.pdf;

import java.io.ByteArrayInputStream;

public interface QuotationPdfService {

    ByteArrayInputStream generateQuotationPdf(Long quotationId);

}