package com.semicolon.campusnestproject.utils;

public class HtmlContent {


    public static String welcomeMessageContent(String name) {
        String url = String.valueOf((HtmlContent.class.getResource("/utils/asset/Campus nest logo.png")));
        return String.format(
                "<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "<head>\n" +
                        "    <meta charset=\"UTF-8\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
                        "    <title>Message</title>\n" +
                        "</head>\n" +
                        "<body>\n" +
                        "<div>\n" +
                        "    <div class=\"logo\" style=\"margin-left: 10%%;\">\n" +
                        "        <img src=\"" + url + "\" alt=\"Campus Nest Logo\" width=\"150\" />\n" +
                        "        <div class=\"heading-container\">\n" +
                        "            <span class=\"heading-1\" style=\"color: #2c3e50; font-size: 50px;\" ><strong>Welcome to </strong></span>\n" +
                        "            <span class=\"heading-2\" style=\"color: #E8734E; font-size: 50px;\"><strong>[Campus </strong> </span>\n" +
                        "            <span class=\"heading-3\" style=\"color: #006FFF; font-size: 50px;\"><strong>Nest] </strong> %s </span> \n" +
                        "        </div>\n" +
                        "        <p>We're thrilled to have you join our community. Here at  <span class=\"heading-2\" style=\"color: #E8734E; font-size: 50px;\"><strong>[Campus </strong> </span>\n" +
                        "            <span class=\"heading-3\" style=\"color: #006FFF; font-size: 50px;\"><strong>Nest] </strong></span>, we strive to make the process of finding reliable tenants as smooth and efficient as possible.</p>\n" +
                        "        <h2 style=\"color: #2c3e50;\">Here's what you can expect:</h2>\n" +
                        "        <ul style=\"padding-left: 20px;\">\n" +
                        "            <li><strong>Easy Listing Creation:</strong> Our user-friendly interface allows you to create detailed and attractive listings for your properties in just a few clicks.</li>\n" +
                        "            <li><strong>Wide Reach:</strong> Reach thousands of potential tenants actively searching for their next home.</li>\n" +
                        "            <li><strong>Secure Communication:</strong> Connect and communicate with prospective tenants through our secure messaging system.</li>\n" +
                        "            <li><strong>Verified Tenants:</strong> Benefit from our tenant verification process to ensure you find trustworthy renters.</li>\n" +
                        "            <li><strong>Support:</strong> Our dedicated support team is always here to assist you with any questions or concerns.</li>\n" +
                        "        </ul>\n" +
                        "        <p>To get started, simply create a listing for your property. Make sure to include high-quality photos and a detailed description to attract the best tenants.</p>\n" +
                        "        <p>If you need any assistance, don’t hesitate to reach out to our support team.</p>\n" +
                        "        <p>Thank you for choosing <span class=\"heading-2\" style=\"color: #E8734E; font-size: 50px;\"><strong>[Campus </strong> </span>\n" +
                        "            <span class=\"heading-3\" style=\"color: #006FFF; font-size: 50px;\"><strong>Nest] </strong></span>. We look forward to helping you find the perfect tenants for your properties!</p>\n" +
                        "        <p>Best regards,<br>The <span class=\"heading-2\" style=\"color: #E8734E; font-size: 50px;\"><strong>[Campus </strong> </span>\n" +
                        "            <span class=\"heading-3\" style=\"color: #006FFF; font-size: 50px;\"><strong>Nest] </strong></span> Team</p>\n" +
                        "    </div>\n" +
                        "</div>\n" +
                        "</body>\n" +
                        "</html>",
                name);
    }
}
