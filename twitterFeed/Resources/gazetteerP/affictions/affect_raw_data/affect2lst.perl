#!/usr/bin/perl

use strict;
use warnings;
use File::Slurp;

my %tag_map = ('a' => 'adjective',
	       'n' => 'noun',
	       'r' => 'adverb',
	       'v' => 'verb');

foreach my $input_file (@ARGV) {
    my $output_file = $input_file;
    $output_file =~ s/\.txt$//;
    $output_file = 'affect_' . $output_file . '.lst';

    print("Processing ", $input_file, " -> ", $output_file, "\n");
    my @list = read_file($input_file);
    my ($line, $tag, @words, $word);
    my @output_list = ();

    while(@list) {
	$line = shift(@list);
	chomp($line);
	if ($line =~ /^(.)#\d+\s+(.*)$/) {
	    $tag = $1;
	    @words = split(/\s+/, $2);
	    foreach $word (@words) {
		push(@output_list, $word . '&category=' . translate_tag($tag) . "\n");
	    }
	}
	else {
	    print("rejected line: ", $line, "\n");
	}
    }

    write_file($output_file, @output_list);
}



sub translate_tag {
    if (defined($tag_map{$_[0]})) {
	return $tag_map{$_[0]};
    }

    # implied else
    return 'unknown';
}
