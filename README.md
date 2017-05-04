# Calen

STDOUTにカレンダーを出力する。

## Installation

    $ git clone https://github.com/gpsoft/calen.git
    $ boot build

## Usage

    $ java -jar target/calen-1.0.0-standalone.jar ARGS

    ARGS: YEAR [MONTH or NUMOFCOLUMNS(in negative integer)]

## Examples

    $ java -jar target/calen-1.0.0-standalone.jar 2017 5
    $ java -jar target/calen-1.0.0-standalone.jar 2017 -2

ロケール指定が必要かも。

    $ LC_ALL=ja_JP.utf8 \
      java -jar target/calen-1.0.0-standalone.jar 2017 5
    2017年5月           
    日 月 火 水 木 金 土
        1  2  3  4  5  6
     7  8  9 10 11 12 13
    14 15 16 17 18 19 20
    21 22 23 24 25 26 27
    28 29 30 31         
