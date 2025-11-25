package com.safetygps.safetygps_server.service.common;

public final class AddressParser {

    private AddressParser() {
    }

    public static AddressComponents parse(String rawAddress) {
        if (rawAddress == null || rawAddress.isBlank()) {
            throw new IllegalArgumentException("주소는 비어 있을 수 없습니다.");
        }

        String trimmed = rawAddress.trim();
        String[] tokens = trimmed.split("\\s+");

        String sigun = null;
        String gu = null;
        String dong = null;

        for (String token : tokens) {
            if (sigun == null && (token.endsWith("시") || token.endsWith("군"))) {
                sigun = token;
                continue;
            }

            if (gu == null && token.endsWith("구")) {
                gu = token;
                continue;
            }

            if (dong == null && (token.endsWith("동")
                    || token.endsWith("읍")
                    || token.endsWith("면")
                    || token.endsWith("리"))) {
                dong = token;
            }
        }

        if (sigun == null && tokens.length > 0) {
            sigun = tokens[0];
        }

        if (dong == null && tokens.length > 0) {
            dong = tokens[tokens.length - 1];
        }

        if (dong == null) {
            throw new IllegalArgumentException("주소에 동/읍/면 정보가 포함되어야 합니다.");
        }

        return new AddressComponents(sigun, gu, dong);
    }
}
