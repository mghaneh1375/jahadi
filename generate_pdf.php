<?php

if ($argc < 3) {
    fwrite(STDERR, "Usage: php generate.php <input.html> <output.pdf>\n");
    exit(1);
}

$inputFile  = $argv[1];
$outputFile = $argv[2];

if (!file_exists($inputFile)) {
    fwrite(STDERR, "Input file not found: $inputFile\n");
    exit(2);
}

require_once __DIR__ . '/vendor/autoload.php';

try {
    $defaultConfig     = (new \Mpdf\Config\ConfigVariables())->getDefaults();
    $fontDirs          = $defaultConfig['fontDir'];

    $defaultFontConfig = (new \Mpdf\Config\FontVariables())->getDefaults();
    $fontData          = $defaultFontConfig['fontdata'];

    $mpdf = new \Mpdf\Mpdf([
        'mode'          => 'utf-8',
        'debug'         => true,
        'format'        => 'A4',
        'fontDir'       => array_merge($fontDirs, [ __DIR__ . '/fonts' ]),
        'fontdata'      => $fontData + [
            'vazirmatn' => [
                'R'          => 'Vazirmatn-Regular.ttf',
                'B'          => 'Vazirmatn-Bold.ttf',
                'useOTL'     => 0xFF,
                'useKashida' => 75,
            ],
        ],
        'default_font'  => 'mitra',
        'tempDir'       => __DIR__ . '/tmp',
        'margin_top'    => 15,
        'margin_bottom' => 15,
        'margin_left'   => 12,
        'margin_right'  => 12,
//         'autoScriptToLang'    => true,
//         'autoLangToFont'      => true,
    ]);

    $html = file_get_contents($inputFile);
    $mpdf->WriteHTML($html);
    $mpdf->Output($outputFile, \Mpdf\Output\Destination::FILE);

    exit(0);

} catch (\Throwable $e) {
    fwrite(STDERR, "mPDF error: " . $e->getMessage() . "\n");
    exit(3);
}