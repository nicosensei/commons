/*
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3.0 of the License, or (at your option) any later version.
 *
 *      http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301 USA
 */
package fr.nikokode.commons.url;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A parsed URL object, that allows access to its domain, host, etc...
 */
public class ParsedUrl {

   /**
    * Separator token.
    */
  private static final String SCHEME_SEP = "://";

  /**
   * Separator token.
   */
  private static final String QUERY_STRING_SEP = "?";

  /**
   * Separator token.
   */
  private static final String HOST_SEGMENT_SEP = ".";

  /**
   * Separator token.
   */
  private static final String HOST_SEGMENT_SEP_REGEX = "\\.";

  /**
   * The scheme.
   */
  private String scheme;

  /**
   * The top level domain.
   */
  private String tld;

  /**
   * The domain level.
   */
  private int domainLevel;

  /**
   * The domain.
   */
  private String domain;

  /**
   * The path segment of the url.
   */
  private String path;

  /**
   * The query string segment of the url.
   */
  private String queryString;

  /**
   * The host segment of the url.
   */
  private String host;

  /**
   * The url validity flag.
   */
  private boolean valid;

  /**
   * COnstructor.
   * @param token the token.
   * @param maxDomainLevel the domain level.
   */
  public ParsedUrl(final String token, final int maxDomainLevel)  {

      this.valid = true;

      String rawUrl = new String(token);

      int schemeSepIndex = rawUrl.indexOf(SCHEME_SEP);
      if (schemeSepIndex != -1) {
          this.scheme = rawUrl.substring(0, schemeSepIndex
                  + SCHEME_SEP.length()).toLowerCase();
      } else {
          this.scheme = "http" + SCHEME_SEP;
          rawUrl = this.scheme + rawUrl;
      }

      this.tld = "";

      this.domain = "";
      this.domainLevel = 0;
      this.path = "";
      this.queryString = "";
      this.host = "";

      try {
          // First validation
          URL urlCheck = new URL(rawUrl);
          this.host = urlCheck.getHost().toLowerCase();
          String[] parts = host.split(HOST_SEGMENT_SEP_REGEX);
          int partCount = parts.length;

          this.tld = "";
          this.domainLevel = 0;

          int startPos = (partCount > 2 ? maxDomainLevel : 1);
          for (int i = startPos; i > 0 && (partCount - i > 0); i--) {
              this.tld += parts[partCount - i] + (i > 1 ? HOST_SEGMENT_SEP : "");
              this.domainLevel++;
          }

          this.domain =
              parts[partCount - this.domainLevel - 1]
                    + HOST_SEGMENT_SEP + this.tld;

          if (this.domain.startsWith("www.")
                  && !("www" + HOST_SEGMENT_SEP + tld).equals(domain)) {
                  // Ex. www.fr -> AFNIC site
              this.domain = this.domain.replaceFirst("www.", "");
          }

          this.domain = this.domain.toLowerCase();
          if (domain.indexOf(HOST_SEGMENT_SEP) == -1) {
              valid = false;
              return;
          }


          if (tld.isEmpty()) {
              valid = false;
              return;
          }

          String pathCheck = urlCheck.getPath();
          this.path = (pathCheck != null ? pathCheck : "");

          String qsCheck = urlCheck.getQuery();
          this.queryString = (qsCheck != null ? qsCheck : "");

          if ("/".equals(path) && queryString.isEmpty()) {
              // Strip terminal slash on host URLs
              this.path = "";
          }

      } catch (final MalformedURLException e) {
          valid = false;
          return;
      }

  }

  /**
   * @return the TLD
   */
  public String getTld() {
      return tld;
  }

  /**
   * @return the domain level
   */
  public int getDomainLevel() {
      return domainLevel;
  }

  /**
   * @return the domain
   */
  public String getDomain() {
      return domain;
  }

  /**
   * @return the URL
   */
  public String getUrl() {
      return scheme + getUrlWithoutScheme();
  }

  /**
   * @return the URL without the scheme
   */
  public String getUrlWithoutScheme() {
      return host + path
           + (queryString.isEmpty() ? "" : QUERY_STRING_SEP + queryString);
  }

  /**
   * @return the scheme
   */
  public String getScheme() {
      return scheme;
  }

  /**
   * @return the path
   */
  public synchronized String getPath() {
      return path;
  }

  /**
   * @return the queryString
   */
  public synchronized String getQueryString() {
      return queryString;
  }

  @Override
  public String toString() {
      return "[" + getUrl() + "][" + domain + " " + domainLevel +
              "][" + tld + "]";
  }

  /**
   * @return true if the URL is a host only
   */
  public boolean isHostUrl() {
      return this.path.isEmpty() && this.queryString.isEmpty();
  }

  /**
   * @return the host
   */
  public String getHost() {
      return host;
  }

  /**
   * @return the host url.
   */
  public String getHostUrl() {
      return scheme + host;

  }

  /**
   * @return the valid
   */
  public boolean isValid() {
      return valid;
  }

}
