# Converts an Oracle SQL script into a mySQL script.
#
# USAGE
#   awk -f oracle-to-mysql.awk oracle.sql > mysql.sql
#
# METHOD
# 1) Strip c-style comment lines (mySQL dislikes these in batch mode)
# 2) Removes Oracle extensions
#    a) CASCADE CONSTRAINTS on DROP TABLE
#    b) Add 'IF EXISTS' on DROP TABLE
# 3) Change Oracle DATE type to mySQL DATETIME type in DDL file
# 4) Strip triggers
# 5) Change function indices to plain column indices
#
BEGIN {
  oraclefile = ARGV[1]
  tmpfile1 = oraclefile ".tmp1"
  tmpfile2 = oraclefile ".tmp2"
  tmpfile3 = oraclefile ".tmp3"
  tmpfile4 = oraclefile ".tmp4"
  strip_c_comments( oraclefile, tmpfile1 )
  close( tmpfile1 );
  change_oracle_specific( tmpfile1, tmpfile2 );
  close( tmpfile2 );
  strip_triggers( tmpfile2, tmpfile3 )
  close( tmpfile3 );
  fix_indexes( tmpfile3, tmpfile4 )
  close( tmpfile4 );
}
END {
  lastWasBlank = 0
  while ( (getline < tmpfile4) > 0 ) {

    if ( NF == 0 && lastWasBlank == 0 ) {
      lastWasBlank = 1
      print $0
    }
    else if ( NF == 0 ) {
      lastWasBlank = 1
    }
    else {
      lastWasBlank = 0
      print $0
    }
  }
}

function strip_c_comments( infile, outfile ) {
  c_comment = 0
  thisLine = ""
  while ( (getline < infile) > 0 ) {
    for ( i=1; i<=NF; i++ ) {

      if ( $i == "/\*" ) {
        c_comment = 1
      }
      else if ( $i == "\*/" ) {
        c_comment = 0
      }
      else if ( c_comment == 0 ) {
        thisLine = thisLine " " $i
      }

    } # for

    print thisLine > outfile
    thisLine = ""
  } # while

} # strip_c_comments

function change_oracle_specific( infile, outfile ) {

  while ( (getline < infile) > 0 ) {
    sub( "CASCADE CONSTRAINTS", "" );
    sub( "DROP TABLE ", "DROP TABLE IF EXISTS " );
    sub( "CHECK .*,$", "," );
    sub( "CHECK .*$", "" );
    sub( " DATE ", " DATETIME " );
    print $0 > outfile
  } # while

} # strip_c_comments

function strip_triggers( infile, outfile ) {
  trigger = 0
  thisLine = ""
  while ( (getline < infile) > 0 ) {
    for ( i=1; i<=NF; i++ ) {

      if ( $i == "create" && $(i+1) == "trigger" ) {
        trigger = 1
      }
      else if ( trigger == 1 && $i == "^/$" ) {
        trigger = 0
      }
      else if ( trigger == 0 ) {
        thisLine = thisLine " " $i
      }

    } # for

    print thisLine > outfile
    thisLine = ""
  } # while

} # strip_triggers

# converts function-indices to plain column indices
function fix_indexes( infile, outfile ) {

  # OK, this is a complete hack...
  # There's only one function-index right now, namely member_account_uk.
  # The 'UPPER' function must be removed, since MySQL doesn't support
  # function-indices. So simply look for a magic phrase and rewrite it.

  while ( (getline < infile) > 0 ) {
    sub( "UPPER.account_name.", "account_name" );
    print $0 > outfile
  }

} # fix_indexes

