#!/bin/bash

create 'table1', {NAME=>'addr'}, {NAME=>'order'}

put   'table1',  'jsmith',  'addr:state', 'TN'
put   'table1',  'jsmith',  'addr:city', 'nashville'
put   'table1',  'jsmith',  'order:numb', '1234'
put   'table1',  'tsimmons',  'addr:city', 'dallas'
put   'table1',  'tsimmons',  'addr:state', 'TX'
put   'table1',  'tsimmons', 'order:numb', '1831'
put   'table1',  'jsmith',  'addr:state', 'CO'
put   'table1',  'jsmith',  'addr:city', 'denver'
put   'table1',  'jsmith',  'order:numb', '6666'
put   'table1',  'njones',  'addr:state', 'TX'
put   'table1',  'njones',  'addr:city', 'miami'
put   'table1',  'njones',  'order:numb', '5555'
put   'table1',  'amiller', 'addr:state', 'TX'
put   'table1',  'amiller', 'addr:city', 'dallas'
put   'table1',  'amiller', 'order:numb', '9986'

create 'table2', {NAME=>'addr'}, {NAME=>'order'}