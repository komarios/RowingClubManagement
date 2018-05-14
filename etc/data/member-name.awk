# Breaks a member-name into component parts; e.g.
#   <member-name>Ernest T. Andalcio</member-name>
# becomes
#   <member-name>
#     <member-name-first>Ernest</member-name-first>
#     <member-name-middle>T.</member-name-middle>
#     <member-name-last>Andalcio</member-name-last>
#     <member-name-suffix></member-name-suffix>
#   <member-name>
#
BEGIN {
}
{
  # Matches <member-name>Last</member-name>
  if ( $1 ~ /.*\/member-name/ ) {
    print $0, "<!-- FLAG -->"
  }
  # Matches <member-name>First Last</member-name>
  else if ( $2 ~ /.*\/member-name./ ) {
    print "<member-name>"
    # Prints stuff to the right of <member-name>
    printf( "  <member-name-first>%s</member-name-first>\n", substr($1,14))
    w = length( $2 )
    printf( "  <member-name-last>%s</member-name-last>\n", substr($2,1,w - 14))
    print "</member-name>"
  }
  # Matches <member-name>First Middle Last</member-name>
  else if ( $3 ~ /.*\/member-name./ ) {
    print "<member-name>"
    # Prints stuff to the right of <member-name>
    printf( "  <member-name-first>%s</member-name-first>\n", substr($1,14))
    printf( "  <member-name-middle>%s</member-name-middle>\n", $2)
    w = length( $3 )
    printf( "  <member-name-last>%s</member-name-last>\n", substr($3,1,w - 14))
    print "</member-name>"
  }
#   # Matches <member-name>First Middle Last Suffix</member-name>
#   else if ( $4 ~ /.*</member-name>/ ) {
#     print $0
#   }
  # Matches other member-name lines
  else if ( $1 ~ /.member-name.*/ ) {
    print $0, "<!-- FLAG -->"
  }
  # Matches non-name lines
  else {
    print $0
  }
}
END {
}
