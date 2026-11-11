param(
    [Parameter(Mandatory=$true)][string]$Log,
    [Parameter(Mandatory=$true)][string]$Output,
    [Parameter(Mandatory=$true)][string]$Title,
    [switch]$Summary
)
$ErrorActionPreference = 'Stop'
Add-Type -AssemblyName System.Drawing
$raw = @(Get-Content -LiteralPath $Log -Encoding UTF8)
if (-not ($raw -match '^\[INFO\] BUILD SUCCESS$')) { throw "Execution non reussie : $Log" }
if ($Summary) {
    $lines = @($raw | Where-Object { $_ -match '^\[INFO\] (Tests run:|Results:|Building jar:|BUILD SUCCESS|Total time:|Finished at:)' })
} else {
    $start = -1
    for ($i=0; $i -lt $raw.Count; $i++) {
        if ($raw[$i] -match '^\[OK\]|^\d+ FAMILLE') { $start=$i; break }
    }
    if ($start -lt 0) { throw 'Aucune sortie de scenario trouvee' }
    $lines = @()
    for ($i=$start; $i -lt $raw.Count; $i++) {
        if ($raw[$i] -match '^\[INFO\]|^Nov |^INFO:|^WARNING:') { break }
        $lines += $raw[$i]
    }
    $lines += ''
    $lines += @($raw | Where-Object { $_ -match '^\[INFO\] (BUILD SUCCESS|Total time:|Finished at:)' })
}
# Rendu des lignes capturees dans le journal, sans inventer de sortie.
$font = New-Object System.Drawing.Font('Consolas', 16)
$titleFont = New-Object System.Drawing.Font('Consolas', 18, [System.Drawing.FontStyle]::Bold)
$lineHeight = 30
$width = 1500
$height = 112 + ($lines.Count * $lineHeight)
$bitmap = New-Object System.Drawing.Bitmap($width, $height)
$graphics = [System.Drawing.Graphics]::FromImage($bitmap)
$graphics.TextRenderingHint = [System.Drawing.Text.TextRenderingHint]::ClearTypeGridFit
$graphics.Clear([System.Drawing.Color]::FromArgb(18,23,33))
$normal = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(226,232,240))
$green = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(110,231,183))
$accent = New-Object System.Drawing.SolidBrush([System.Drawing.Color]::FromArgb(147,197,253))
try {
    $graphics.DrawString($Title, $titleFont, $accent, 28, 24)
    $y = 86
    foreach ($line in $lines) {
        $brush = if ($line -match '^\[OK\]|BUILD SUCCESS|Tests run:.*Failures: 0, Errors: 0') { $green } else { $normal }
        $graphics.DrawString($line, $font, $brush, 28, $y)
        $y += $lineHeight
    }
    $bitmap.Save([System.IO.Path]::GetFullPath($Output), [System.Drawing.Imaging.ImageFormat]::Png)
    [System.IO.File]::WriteAllLines([System.IO.Path]::ChangeExtension([System.IO.Path]::GetFullPath($Output), '.txt'), [string[]]$lines, (New-Object System.Text.UTF8Encoding($false)))
} finally {
    $graphics.Dispose()
    $bitmap.Dispose()
    $font.Dispose()
    $titleFont.Dispose()
    $normal.Dispose()
    $green.Dispose()
    $accent.Dispose()
}
