% resume.cls
%
%    Gilles Besançon
%
%    Customized from
%    Copyright 2005 Daniel Burrows
%
% You may use, modify, and redistribute this file in any way you please, so
% long as this copyright notice is maintained.


\NeedsTeXFormat{LaTeX2e}
\ProvidesClass{resume/resume}

\LoadClassWithOptions{article}

% No page numbers by default.
\pagestyle{empty}

\RequirePackage{hyperref}
\RequirePackage{calc}
\RequirePackage{ifthen}
\RequirePackage{url}
\RequirePackage{tabularx}
\RequirePackage{pgf}
\RequirePackage{xxcolor}
\RequirePackage{graphicx}
\RequirePackage{times}
\RequirePackage[T1]{fontenc}
\RequirePackage[utf8x]{inputenc}

\ProcessOptions

\definecolor{rulestartcolor}{rgb}{0,0,0}
\definecolor{ruleendcolor}{rgb}{0.4,0.4,0.4}

\addtolength{\oddsidemargin}{-1.6in}
\addtolength{\evensidemargin}{-1.2in}
\addtolength{\textwidth}{2.8in}
\addtolength{\topmargin}{-1.5in}
\addtolength{\bottommargin}{-1.5in}
\addtolength{\textheight}{3.0in}

\newlength{\iconsize}%
\setlength{\iconsize}{5mm}

\newlength{\titlespace}%
\setlength{\titlespace}{0.5pt}

\newlength{\beforesectionspace}%
\setlength{\beforesectionspace}{6pt}
\newlength{\aftersectionspace}%
\setlength{\aftersectionspace}{3pt}

\newlength{\beforejobspace}%
\setlength{\beforejobspace}{3pt}
\newlength{\afterjobspace}%
\setlength{\afterjobspace}{1pt}

\newlength{\beforeassignmentspace}%
\setlength{\beforeassignmentspace}{3pt}
\newlength{\afterassignmentspace}%
\setlength{\afterassignmentspace}{1pt}

\newlength{\@rulewidth}%
\newlength{\@ruleheader}%

% Usage: \@setupshading{width}{height}{start}{end}
\newcommand{\@setupshading}[4]{%
  \pgfdeclarehorizontalshading{@ruleshading}{#2}{%
    color(0pt)=(#3); color(#1)=(#4)%
  }%
}%

\newcommand{\pgfrule}[4]{%
  \begin{pgfpicture}{0pt}{0pt}{#1}{#2}%
    \pgfdeclarehorizontalshading{@ruleshading}{#2}{%
      color(0pt)=(#3); color(#1)=(#4)%
    }
    \pgfputat{\pgfxy(0,0)}{\pgfbox[left,bottom]{\pgfuseshading{@ruleshading}}}
  \end{pgfpicture}%
}%

\newcommand{\lrule}[2]{\pgfrule{#1}{#2}{rulestartcolor}{ruleendcolor}}
\newcommand{\rrule}[2]{\pgfrule{#1}{#2}{ruleendcolor}{rulestartcolor}}

\newcommand{\leftrule}[2]{%
  \setlength{\@rulewidth}{\linewidth * #1}%
  \goodbreak\vspace{\titlespace}%
  \lrule{\@rulewidth}{#2}%
  \goodbreak\vspace{\titlespace}%
}%

\newcommand{\centerrule}[2]{%
  \setlength{\@rulewidth}{\linewidth * #1}%
  \setlength{\@ruleheader}{(\linewidth-\@rulewidth)/2}%
  \goodbreak\vspace{\titlespace}%
  \hspace{\@ruleheader}\lrule{\@rulewidth}{#2}%
  \goodbreak\vspace{\titlespace}%
}%

\newcommand{\rightrule}[2]{%
  \setlength{\@rulewidth}{\linewidth * #1}%  
  \setlength{\@ruleheader}{\linewidth-\@rulewidth}%
  \goodbreak\vspace{\titlespace}%
  \hspace{\@ruleheader}\rrule{\@rulewidth}{#2}%
  \goodbreak\vspace{\titlespace}%
}%

\newcommand{\email}[1]{\includegraphics[width=\iconsize]{resume/email.png} #1}
\newcommand{\address}[2]{\includegraphics[width=\iconsize]{resume/address.png} #1 #2}
\newcommand{\phone}[1]{\includegraphics[width=\iconsize]{resume/phone.jpg} #1}
\newcommand{\skype}[1]{\includegraphics[width=\iconsize]{resume/skype.jpg} #1}
\newcommand{\webpage}[1]{\href{#1}}
% Birth - Usage: \birth{date}{location}
\newcommand{\birth}[2]{#1 - #2}
% Social Networks
\newcommand{\twitter}[1]{\includegraphics[width=\iconsize]{resume/twitter.png} #1}
\newcommand{\facebook}[1]{\includegraphics[width=\iconsize]{resume/facebook.png} #1}
\newcommand{\googleplus}[1]{\includegraphics[width=\iconsize]{resume/googleplus.png} #1}
\newcommand{\linkedin}[1]{\includegraphics[width=\iconsize]{resume/linkedin.png} #1}
% Languages
\newcommand{\french}[1]{\includegraphics[width=\iconsize]{resume/france.png} #1}
\newcommand{\englishuk}[1]{\includegraphics[width=\iconsize]{resume/united_kingdom.png} #1}
\newcommand{\englishus}[1]{\includegraphics[width=\iconsize]{resume/united_states_of_america.png} #1}
\newcommand{\spanish}[1]{\includegraphics[width=\iconsize]{resume/spain.png} #1}

% Section - Usage: \section{title}
\renewcommand{\section}[1]{%
  \goodbreak\vspace{\beforesectionspace}%
  \par\nointerlineskip\rightrule{2/3}{1pt}%
  \goodbreak\vspace{\titlespace}%
  \par\nointerlineskip\noindent\makebox[\linewidth][c]{\textbf{\textsc{\Large #1}}}
  \goodbreak\vspace{\titlespace}%
  \par\nointerlineskip\leftrule{2/3}{1pt}%
  \goodbreak\vspace{\aftersectionspace}%
}

% Job - Usage: \job[title]{institution}{years}
\newcommand{\job}[3][]{%
  \goodbreak\vspace{\beforejobspace}%
  \begin{tabularx}{\linewidth}{l X r}%
    \bfseries #3 & \ifthenelse{\equal{#1}{}}{}{\bfseries #1} & \bfseries #2%
  \end{tabularx}%
  \goodbreak\vspace{\afterjobspace}%
}%

% Assignment - Usage: \assignment[title]{institution}{years}
\newcommand{\assignment}[3][]{%
  \goodbreak\vspace{\beforeassignmentspace}%
  \begin{tabularx}{\linewidth}{c l X r}%
    \bfseries - & \bfseries \small \itshape #3 & \bfseries \itshape #1 & \bfseries \small \itshape #2%
  \end{tabularx}%
  \goodbreak\vspace{\afterassignmentspace}%
}%